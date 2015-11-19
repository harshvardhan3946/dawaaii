package com.dawaaii.service.dao.mongo;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by rohit on 3/11/15.
 */
public interface AmbulanceRepository extends MongoRepository<Ambulance, String> {

    List<Ambulance> findByGeoJsonPoint(GeoJsonPoint geoJsonPoint);

    List<Ambulance> findByCity(String city);
}
