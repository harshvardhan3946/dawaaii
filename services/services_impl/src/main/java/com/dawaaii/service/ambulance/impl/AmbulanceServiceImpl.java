package com.dawaaii.service.ambulance.impl;

import com.dawaaii.service.dao.mongo.AmbulanceRepository;
import com.dawaaii.service.mongo.ambulance.AmbulanceService;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.notification.sms.SMSSenderService;
import com.dawaaii.service.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rohit on 3/11/15.
 */
@Service(value = "ambulanceService")
public class AmbulanceServiceImpl implements AmbulanceService {

    private AmbulanceRepository ambulanceRepository;

    private EmailService emailService;

    private SMSSenderService smsSenderService;

    @Autowired
    public AmbulanceServiceImpl(AmbulanceRepository ambulanceRepository, EmailService emailService, SMSSenderService smsSenderService){
        this.ambulanceRepository = ambulanceRepository;
        this.emailService = emailService;
        this.smsSenderService = smsSenderService;
    }
    @Override
    public Ambulance save(Ambulance ambulance) {
        return ambulanceRepository.save(ambulance);
    }

    @Override
    public Ambulance getById(String id) {
        return ambulanceRepository.findOne(id);
    }

    @Override
    public List<Ambulance> get() {
        return ambulanceRepository.findAll();
    }

    @Override
    public List<Ambulance> getByLocationNear(Point point) {
        return ambulanceRepository.findByPointNear(point);
    }

    @Override
    public Long getCount() {
        return ambulanceRepository.count();
    }

    @Override
    public void confirmBooking(Ambulance ambulance, User user) {
        emailService.sendConfirmBookingEmailToUser(user);
        emailService.sendConfirmBookingEmailToAmbulance(ambulance);
        smsSenderService.sendConfirmBookingSMSToUser(ambulance,user);
        smsSenderService.sendConfirmBookingSMSToAmbulance(user,ambulance);

        //ToDO 1) Send Email to the ambulance service provider and user
        //Todo 2) Send sms to user and ambulance number


    }
}
