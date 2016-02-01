package com.eutechpro.allytransitapp.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RouteStop {
    @JsonProperty("name")
    private String name;
    @JsonProperty("properties")
    private String properties;
    @JsonProperty("datetime")
    private String dateTime;
    @JsonProperty("lat")
    private double lat;
    @JsonProperty("lng")
    private double lng;

    public String getName() {
        return name;
    }

    public String getProperties() {
        return properties;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
