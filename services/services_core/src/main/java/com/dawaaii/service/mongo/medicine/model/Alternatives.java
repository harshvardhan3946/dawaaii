package com.dawaaii.service.mongo.medicine.model;

/**
 * Created by hojha on 11/01/16.
 */
public class Alternatives {

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

    public Alternatives() {
    }

    public Alternatives(String brand, String category, String dClass, String unitType, String unitQty, String packageType, String packageQty, String packagePrice, String unitPrice, String genericId) {
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
    }

    public Alternatives(com.truemd.Medicine medicine) {
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
}
