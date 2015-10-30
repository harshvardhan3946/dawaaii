package com.dawaaii.test;

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
 * Created by root on 29/10/15.
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
    private InsertUserData insertUserData;

    @PostConstruct
    @Transactional
    public void insertReferenceData() {
        if (isFillData()) {
            //TODO: insert ref data here.

            insertUserData.insertData();

        }
    }

    private boolean isFillData() {
        return userService.getUsersCount() == 0;
    }

}
