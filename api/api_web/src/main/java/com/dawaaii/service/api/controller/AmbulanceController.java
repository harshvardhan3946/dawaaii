package com.dawaaii.service.api.controller;

import com.dawaaii.service.mongo.ambulance.AmbulanceService;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.user.UserService;
import com.dawaaii.service.user.model.User;
import com.dawaaii.web.common.model.BookAmbulanceViewModel;
import com.dawaaii.web.common.response.DawaaiiApiResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dawaaii.web.common.response.DawaaiiApiResponse.error;
import static com.dawaaii.web.common.response.DawaaiiApiResponse.success;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by rohit on 5/11/15.
 */
@Api(value = "/ambulances", description = "ambulance operations")
@RequestMapping(value = "/ambulances")
@Controller
public class AmbulanceController {

    private final static Logger LOG = LoggerFactory.getLogger(AmbulanceController.class);

    private AmbulanceService ambulanceService;

    private UserService userService;

    @Autowired
    public AmbulanceController(AmbulanceService ambulanceService, UserService userService){
        this.ambulanceService = ambulanceService;
        this.userService = userService;
    }

    @ApiOperation(value = "get ambulance list")
    @RequestMapping(method = GET)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> getAllAmbulances(){
        return success().withEntity("Ambulances",ambulanceService.get()).respond();
    }

    @ApiOperation(value = "get Ambulance list sorted by location")
    @RequestMapping(value = "/sorted", method = GET)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> getAllAmbulancesSortedByLocation(@RequestParam(value = "lat") double lat,@RequestParam(value = "lon") double lon ){
        return success().withEntity("Ambulances",ambulanceService.getByLocationNear(new Point(lat,lon))).respond();
    }

    @ApiOperation(value = "book an ambulance")
    @RequestMapping(value = "/book",method = POST)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> bookAmbulance(@RequestBody BookAmbulanceViewModel bookAmbulanceViewModel){
        try {
            //User user = userService.getUserByEmail(bookAmbulanceViewModel.getEmail());
            Ambulance ambulance = ambulanceService.getById(bookAmbulanceViewModel.getAmbulanceId());
            if(ambulance == null || bookAmbulanceViewModel.getEmail()==null || bookAmbulanceViewModel.getNumber()==null){
                return error("Either User or Ambulance not present", HttpStatus.BAD_REQUEST).respond();
            }
            ambulanceService.confirmBooking(bookAmbulanceViewModel.getEmail(), bookAmbulanceViewModel.getNumber(), ambulance);
        }catch (Exception e){
            return error("Ambulance could not be booked due to exception " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR).respond();
        }
        return success("Ambulance booked successfully").respond();
    }

    @ApiOperation(value = "get Ambulance by city")
    @RequestMapping(value = "/{city}",method = GET)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> getAmbulanceByCity(@PathVariable String city){
        try {
            List<Ambulance> ambulanceList = ambulanceService.getByCity(city);
            return success().withEntity("Ambulances",ambulanceList).respond();
        }catch (Exception e){
            return error("Ambulance could not be booked due to exception " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR).respond();
        }
    }
}