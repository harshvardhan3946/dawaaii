package com.dawaaii.service.dao.mongo;

import com.dawaaii.service.mongo.medicine.model.Medicine;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hojha on 15/01/16.
 */
public interface MedicineRepository extends MongoRepository<Medicine, String>{

    Medicine findByBrand(String brand);
}
