package com.dawaaii.service.medicine.impl;

import com.dawaaii.service.dao.mongo.MedicineRepository;
import com.dawaaii.service.mongo.medicine.MedicineService;
import com.dawaaii.service.mongo.medicine.model.Alternatives;
import com.dawaaii.service.mongo.medicine.model.Medicine;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.truemd.TrueMDAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hojha on 09/01/16.
 */
@Service
@Lazy
public class MedicineServiceImpl implements MedicineService {

    private static final String EMPTY_STRING = "";

    @Autowired
    private MedicineRepository medicineRepository;

    @Value("${online.medicine.truemd.api.key}")
    private String apiKey;//8bdb823ced65ee20c8925b381f5390

    private TrueMDAPI trueMDAPI;

    private static final RadixTree<String> radixTree = new ConcurrentRadixTree<>(new DefaultCharArrayNodeFactory());

    @PostConstruct
    public void buildMedicineSuggestion() throws IOException {
        trueMDAPI.key = apiKey;
        JsonNode suggestions = new ObjectMapper().readTree(new ClassPathResource("medicines.json").getInputStream()).get("suggestions");
        suggestions.iterator().forEachRemaining(s -> {
            String medicine = s.get("suggestion").toString().replaceAll("\"", EMPTY_STRING);
            radixTree.put(medicine, medicine);
        });
    }

    @Override
    public Iterable<CharSequence> suggestMedicine(String medicineName) {
        return radixTree.getKeysStartingWith(medicineName);
    }

    public Medicine getMedicineDetails(String name) {
        Medicine medicine = medicineRepository.findByBrand(name);
        if (medicine == null) {
            com.truemd.Medicine med = trueMDAPI.getMedicineData(name);
            if (med != null) {
                Medicine model = new Medicine(med);
                HashMap<String, com.truemd.Medicine> medicineAlternatives = trueMDAPI.getMedicineAlternatives(name);
                if (medicineAlternatives != null) {
                    List<Alternatives> alternatives = medicineAlternatives.values().stream().filter(m -> !m.getGenericId().equals(model.getGenericId())).map(Alternatives::new).collect(Collectors.toList());
                    model.setAlternatives(alternatives);
                }
                return medicineRepository.save(model);
            }
        }
        return medicine;
    }
}