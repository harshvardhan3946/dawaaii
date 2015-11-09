package com.dawaaii.service.ambulance.impl;

import com.dawaaii.service.dao.mongo.AmbulanceRepository;
import com.dawaaii.service.mongo.ambulance.AmbulanceService;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rohit on 3/11/15.
 */
@Service(value = "ambulanceService")
public class AmbulanceServiceImpl implements AmbulanceService {

    private AmbulanceRepository ambulanceRepository;

    @Autowired
    public AmbulanceServiceImpl(AmbulanceRepository ambulanceRepository){
        this.ambulanceRepository = ambulanceRepository;
    }
    @Override
    public Ambulance saveAmbulance(Ambulance ambulance) {
        return ambulanceRepository.save(ambulance);
    }

    @Override
    public Ambulance getAmbulanceById(String id) {
        return ambulanceRepository.findOne(id);
    }

    @Override
    public List<Ambulance> getAmbulance() {
        return ambulanceRepository.findAll();
    }

    @Override
    public Long getAmbulanceCount() {
        return ambulanceRepository.count();
    }

    @Override
    public void confirmBooking(Ambulance ambulance) {
        //ToDO 1) Send Email to the ambulance service provider and user
        //Todo 2) Send sms to user and ambulance number


    }
}
