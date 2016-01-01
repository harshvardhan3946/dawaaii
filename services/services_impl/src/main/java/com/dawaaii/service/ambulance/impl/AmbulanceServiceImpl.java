package com.dawaaii.service.ambulance.impl;

import com.dawaaii.service.dao.mongo.AmbulanceRepository;
import com.dawaaii.service.mongo.ambulance.AmbulanceBookingService;
import com.dawaaii.service.mongo.ambulance.AmbulanceService;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.mongo.ambulance.model.AmbulanceBooking;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.notification.sms.SMSSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rohit on 3/11/15.
 */
@Service(value = "ambulanceService")
public class AmbulanceServiceImpl implements AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;

    private final EmailService emailService;

    private final SMSSenderService smsSenderService;

    @Autowired
    private AmbulanceBookingService ambulanceBookingService;

    @Autowired
    public AmbulanceServiceImpl(AmbulanceRepository ambulanceRepository, EmailService emailService, SMSSenderService smsSenderService) {
        this.ambulanceRepository = ambulanceRepository;
        this.emailService = emailService;
        this.smsSenderService = smsSenderService;
    }

    @Override
    public Ambulance save(Ambulance ambulance) {
        return ambulanceRepository.save(ambulance);
    }

    @Override
    public Ambulance getByEmail(String email) {
        return ambulanceRepository.findByEmail(email);
    }

    @Override
    public Ambulance getById(String id) {
        return ambulanceRepository.findOne(id);
    }

    @Override
    public List<Ambulance> getAll() {
        return ambulanceRepository.findAll();
    }

    @Override
    public List<Ambulance> getByLocationNear(Point point) {
        return ambulanceRepository.findByPointNear(point);
    }

    @Override
    public List<Ambulance> getByCity(String city) {
        return ambulanceRepository.findByCity(city);
    }

    @Override
    public Long getCount() {
        return ambulanceRepository.count();
    }

    @Override
    public void confirmBooking(String userEmail, String userName, String userNumber, Ambulance ambulance) {

        smsSenderService.sendConfirmBookingSMSToUser(userName, userNumber, ambulance);
        smsSenderService.sendConfirmBookingSMSToAmbulance(userName, userNumber, ambulance);
        emailService.sendConfirmBookingEmailToUser(userEmail, userName, ambulance);
        emailService.sendConfirmBookingEmailToAmbulance(userEmail, userName, userNumber, ambulance);

        ambulanceBookingService.auditBooking(new AmbulanceBooking(userName, userEmail, userNumber, ambulance.getId()));

    }
}
