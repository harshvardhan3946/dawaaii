package com.dawaaii.service.dao.mongo;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by rohit on 3/11/15.
 */
public interface AmbulanceRepository extends MongoRepository<Ambulance, String> {

    List<Ambulance> findByPointNear(Point location);

    List<Ambulance> findByCity(String city);

    Ambulance findByEmail(String email);
}