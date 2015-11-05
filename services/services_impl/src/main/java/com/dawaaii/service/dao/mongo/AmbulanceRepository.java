package com.dawaaii.service.dao.mongo;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by root on 3/11/15.
 */
public interface AmbulanceRepository extends MongoRepository<Ambulance,String> {

}
