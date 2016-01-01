package com.dawaaii.service.mongo.ambulance;

import com.dawaaii.service.mongo.ambulance.model.AmbulanceBooking;

/**
 * Created by hojha on 01/01/16.
 */
public interface AmbulanceBookingService {

    AmbulanceBooking auditBooking(AmbulanceBooking ambulanceBooking);
}
