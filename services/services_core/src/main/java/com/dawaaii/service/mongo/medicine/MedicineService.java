package com.dawaaii.service.mongo.medicine;

import com.dawaaii.service.mongo.medicine.model.Medicine;

/**
 * Created by hojha on 09/01/16.
 */
public interface MedicineService {

    Iterable<CharSequence> suggestMedicine(String medicineName);

    Medicine getMedicineDetails(String name);
}
