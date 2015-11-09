package com.dawaaii.service.mongo.ambulance;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;

import java.util.List;

/**
 * Created by root on 3/11/15.
 */
public interface AmbulanceService {

    Ambulance saveAmbulance(Ambulance ambulance);

    Ambulance getAmbulanceById(String ambulanceId);

    List<Ambulance> getAmbulance();

    Long getAmbulanceCount();

    void confirmBooking(Ambulance ambulance);

}
