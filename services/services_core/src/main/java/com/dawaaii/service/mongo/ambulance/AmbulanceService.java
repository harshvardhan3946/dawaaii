package com.dawaaii.service.mongo.ambulance;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.user.model.User;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;

import java.util.List;

/**
 * Created by rohit on 3/11/15.
 */
public interface AmbulanceService {

    Ambulance save(Ambulance ambulance);

    Ambulance getById(String ambulanceId);

    List<Ambulance> get();

    List<Ambulance> getByLocationNear(Point point);

    List<Ambulance> getByCity(String city);

    Long getCount();

    void confirmBooking(String email, String number, Ambulance ambulance);

}
