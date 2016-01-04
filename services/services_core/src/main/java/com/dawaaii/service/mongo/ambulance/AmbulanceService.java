package com.dawaaii.service.mongo.ambulance;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import org.springframework.data.geo.Point;

import java.util.List;

/**
 * Created by rohit on 3/11/15.
 */
public interface AmbulanceService {

    Ambulance save(Ambulance ambulance);

    Ambulance getByEmail(String email);

    Ambulance getById(String ambulanceId);

    List<Ambulance> getAll();

    List<Ambulance> getByLocationNear(Point point);

    List<Ambulance> getByCity(String city);

    Long getCount();

    void confirmBooking(String email, String name, String number, String address, Ambulance ambulance);

    List<Ambulance> getUpdatedAfter(Long timeStamp);
}
