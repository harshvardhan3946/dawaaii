package com.dawaaii.service.api.controller;

import com.dawaaii.service.mongo.ambulance.AmbulanceService;
import com.dawaaii.web.common.response.DawaaiiApiResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.dawaaii.web.common.response.DawaaiiApiResponse.success;

/**
 * Created by rohit on 5/11/15.
 */

@Api(value = "/ambulances", description = "ambulance operations")
@RequestMapping(value = "/ambulances")
@Controller
public class AmbulanceController {
private final static Logger LOG = LoggerFactory.getLogger(AmbulanceController.class);
private AmbulanceService ambulanceService;
    @Autowired
    public AmbulanceController(AmbulanceService ambulanceService){
        this.ambulanceService = ambulanceService;
    }
    @ApiOperation(value = "get ambulance list")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> getAllAmbulances(){
        return success().withEntity("Ambulances",ambulanceService.getAmbulance()).respond();
    }
}