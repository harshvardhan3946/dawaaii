package com.dawaaii.ambulance;

import com.dawaaii.service.mongo.ambulance.AmbulanceService;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.test.AbstractDawaaiiServiceBaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rohit on 3/11/15.
 */
public class AmbulanceServiceTest extends AbstractDawaaiiServiceBaseIntegrationTest {
    @Autowired
    private AmbulanceService ambulanceService;

    @Test
    public void saveAmbulanceTest(){
        Ambulance ambulance = new Ambulance();
        ambulance.setServiceProviderName("DAWAAII");
        ambulance.setDescription("this is a new description");
        ambulance.setCity("Delhi");
        ambulance.setArea("NOIDA");
        ambulance.setAddress("B/44 Ground Floor");
        ambulance.setContactNumber("22-1099898");
        ambulance.setMobileNumber("9999999999");

        ambulance = ambulanceService.saveAmbulance(ambulance);
        Assert.assertNotNull(ambulance);
    }

}
