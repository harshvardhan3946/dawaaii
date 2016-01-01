package com.dawaaii.service.dao.mongo;

import com.dawaaii.service.mongo.ambulance.model.AmbulanceBooking;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hojha on 01/01/16.
 */
public interface AmbulanceBookingsRepository extends MongoRepository<AmbulanceBooking, String> {
}
