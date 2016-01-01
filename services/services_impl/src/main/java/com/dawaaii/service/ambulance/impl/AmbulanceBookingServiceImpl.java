package com.dawaaii.service.ambulance.impl;

import com.dawaaii.service.dao.mongo.AmbulanceBookingsRepository;
import com.dawaaii.service.mongo.ambulance.AmbulanceBookingService;
import com.dawaaii.service.mongo.ambulance.model.AmbulanceBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hojha on 01/01/16.
 */
@Service
public class AmbulanceBookingServiceImpl implements AmbulanceBookingService {

    @Autowired
    private AmbulanceBookingsRepository ambulanceBookingsRepository;

    @Override
    public AmbulanceBooking auditBooking(AmbulanceBooking ambulanceBooking) {
        return ambulanceBookingsRepository.save(ambulanceBooking);
    }
}
