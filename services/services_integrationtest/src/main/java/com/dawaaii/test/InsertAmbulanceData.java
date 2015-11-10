package com.dawaaii.test;

import com.dawaaii.service.mongo.ambulance.AmbulanceService;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by rohit on 3/11/15.
 */
@Component
public class InsertAmbulanceData {

    @Autowired
    private AmbulanceService ambulanceService;

    public void insertData(){
        Ambulance ambulance = createAmbulanceObject();
        ambulanceService.save(ambulance);
    }

    private Ambulance createAmbulanceObject(){
        Ambulance ambulance = new Ambulance();
        ambulance.setServiceProviderName("DAWAAII");
        ambulance.setDescription("this is a new description");
        ambulance.setCity("Delhi");
        ambulance.setArea("NOIDA");
        ambulance.setAddress("B/44 Ground Floor");
        ambulance.setContactNumber("22-1099898");
        ambulance.setMobileNumber("9999999999");
        return ambulance;
    }
}
