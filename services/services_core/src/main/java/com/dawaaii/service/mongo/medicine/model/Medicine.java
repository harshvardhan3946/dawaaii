package com.dawaaii.service.mongo.medicine.model;

import com.dawaaii.service.mongo.BaseDocument;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hojha on 11/01/16.
 */
@Document(collection = "medicines")
public class Medicine extends BaseDocument {

    @Indexed
    private String brand;
    private String category;
    private String dClass;
    private String unitType;
    private String unitQty;
    private String packageType;
    private String packageQty;
    private String packagePrice;
    private String unitPrice;
    private String genericId;
    private List<Alternatives> alternatives;

    public Medicine() {
    }

    public Medicine(String brand, String category, String dClass, String unitType, String unitQty, String packageType, String packageQty, String packagePrice, String unitPrice, String genericId, List<Alternatives> alternatives) {
        this.brand = brand;
        this.category = category;
        this.dClass = dClass;
        this.unitType = unitType;
        this.unitQty = unitQty;
        this.packageType = packageType;
        this.packageQty = packageQty;
        this.packagePrice = packagePrice;
        this.unitPrice = unitPrice;
        this.genericId = genericId;
        this.alternatives = alternatives;
    }

    public Medicine(com.truemd.Medicine medicine) {
        this.brand = medicine.getBrand();
        this.category = medicine.getCategory();
        this.dClass = medicine.getDClass();
        this.unitType = medicine.getUnitType();
        this.unitQty = medicine.getUnitQty();
        this.packageType = medicine.getPackageType();
        this.packageQty = medicine.getPackageQty();
        this.packagePrice = medicine.getPackagePrice();
        this.unitPrice = medicine.getUnitPrice();
        this.genericId = medicine.getGenericId();
        this.alternatives = new ArrayList<>();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getdClass() {
        return dClass;
    }

    public void setdClass(String dClass) {
        this.dClass = dClass;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(String unitQty) {
        this.unitQty = unitQty;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackageQty() {
        return packageQty;
    }

    public void setPackageQty(String packageQty) {
        this.packageQty = packageQty;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getGenericId() {
        return genericId;
    }

    public void setGenericId(String genericId) {
        this.genericId = genericId;
    }

    public List<Alternatives> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Alternatives> alternatives) {
        this.alternatives = alternatives;
    }
}
