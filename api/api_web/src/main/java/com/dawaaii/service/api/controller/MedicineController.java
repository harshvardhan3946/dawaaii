package com.dawaaii.service.api.controller;

import com.dawaaii.service.mongo.medicine.MedicineService;
import com.dawaaii.web.common.response.DawaaiiApiResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.dawaaii.web.common.response.DawaaiiApiResponse.success;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by hojha on 13/01/16.
 */
@Api(value = "/medicines", description = "online medicine operations")
@RequestMapping(value = "/medicines")
@Controller
public class MedicineController {

    private final static Logger LOG = LoggerFactory.getLogger(MedicineController.class);

    @Autowired
    private MedicineService medicineService;

    @ApiOperation(value = "get medicine suggestions")
    @RequestMapping(value = "/{medicineName}", method = GET)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> getMedicineSuggestion(@PathVariable String medicineName) {
        LOG.debug("suggesting auto complete for "+medicineName);
        return success().withEntity("suggestions", medicineService.suggestMedicine(medicineName)).respond();
    }

    @ApiOperation(value = "get medicine suggestions")
    @RequestMapping(value = "detail/{medicineName}", method = GET)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> getMedicineDetails(@PathVariable String medicineName) {
        LOG.debug("finding details for "+medicineName);
        return success().withEntity("details", medicineService.getMedicineDetails(medicineName)).respond();
    }
}
