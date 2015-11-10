package com.dawaaii.service.ambulance.impl;


import com.dawaaii.service.dao.mongo.AmbulanceRepository;
import com.dawaaii.service.mongo.ambulance.AmbulanceService;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by rohit on 29/10/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AmbulanceServiceImplTest.class})
public class AmbulanceServiceImplTest{
    @Mock
    private AmbulanceRepository ambulanceRepository;

    private AmbulanceService ambulanceService;

    @Before
    public void setUp(){
        ambulanceService = new AmbulanceServiceImpl(ambulanceRepository);
    }

    @Test
    public void shouldSaveValidAmbulance(){
        Ambulance ambulance = new Ambulance();
        ambulance.setServiceProviderName("DAWAAII");
        ambulance.setDescription("this is a new description");
        ambulance.setCity("Delhi");
        ambulance.setArea("NOIDA");
        ambulance.setAddress("B/44 Ground Floor");
        ambulance.setContactNumber("22-1099898");
        ambulance.setMobileNumber("9999999999");
        when(ambulanceRepository.save(ambulance)).thenReturn(ambulance);
        ambulanceService.save(ambulance);
        verify(ambulanceRepository,times(1)).save(ambulance);
    }
}
