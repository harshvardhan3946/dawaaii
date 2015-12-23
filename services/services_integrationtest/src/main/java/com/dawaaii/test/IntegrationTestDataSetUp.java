package com.dawaaii.test;

import com.dawaaii.service.mongo.ambulance.AmbulanceService;
import com.dawaaii.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by rohit on 29/10/15.
 */
@Component
@Lazy(value = false)
public class IntegrationTestDataSetUp {

    private final static Logger LOGGER = LoggerFactory.getLogger(IntegrationTestDataSetUp.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AmbulanceService ambulanceService;

    @Autowired
    private InsertUserData insertUserData;

    @Autowired
    private InsertAmbulanceData insertAmbulanceData;

    @PostConstruct
    @Transactional
    public void insertReferenceData() {
        if (isFillUserData()) {
            insertUserData.insertData();
        }
        if (isFillAmbulanceData()) {
            insertAmbulanceData.insertData();
        }
    }

    private boolean isFillUserData() {
        return userService.getUsersCount() == 0;
    }
    private boolean isFillAmbulanceData() {
        return ambulanceService.getCount() == 0;
    }

}
