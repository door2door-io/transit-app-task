package com.eutechpro.allytransitapp.data.model.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class CarSharingRouteProperties {
    private String address;
    private String model;
    @JsonProperty("license_plate")
    private String licencePlate;
    @JsonProperty("fuel_level")
    private int fuelLevel;
    @JsonProperty("engine_type")
    private String engineType;
    @JsonProperty("internal_cleanliness")
    private String internalCleanliness;
    private String description;
    private int seats;
    private int doors;

    public String getAddress() {
        return address;
    }

    public String getModel() {
        return model;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public String getEngineType() {
        return engineType;
    }

    public String getInternalCleanliness() {
        return internalCleanliness;
    }

    public String getDescription() {
        return description;
    }

    public int getSeats() {
        return seats;
    }

    public int getDoors() {
        return doors;
    }
}
