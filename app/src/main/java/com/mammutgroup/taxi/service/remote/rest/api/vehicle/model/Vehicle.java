package com.mammutgroup.taxi.service.remote.rest.api.vehicle.model;

import com.google.gson.annotations.SerializedName;
import com.mammutgroup.taxi.service.remote.model.BaseDto;

/**
 * Created by Iraj on 6/9/2016.
 */
public class Vehicle extends BaseDto {

    @SerializedName("plateNumber")
    private String plateNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("capacity")
    private String capacity;
    @SerializedName("trafficLicense")
    private String trafficLicense;
    @SerializedName("chassisNumber")
    private String chassisNumber;
    @SerializedName("vinNumber")
    private String vinNumber;
    @SerializedName("brand")
    private String brand;
    @SerializedName("model")
    private String model;
    @SerializedName("year")
    private int year;

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getTrafficLicense() {
        return trafficLicense;
    }

    public void setTrafficLicense(String trafficLicense) {
        this.trafficLicense = trafficLicense;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
