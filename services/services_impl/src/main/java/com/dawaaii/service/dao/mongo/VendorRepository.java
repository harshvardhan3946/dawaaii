package com.dawaaii.service.dao.mongo;

import com.dawaaii.service.mongo.vendor.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by rohit on 9/1/16.
 */
public interface VendorRepository extends MongoRepository<Vendor,String> {
}
