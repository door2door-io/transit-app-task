package com.eutechpro.allytransitapp.data.model;

import android.support.annotation.StringDef;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class RouteSegment {
    @StringDef({
            TRAVEL_MODE_WALKING,
            TRAVEL_MODE_SUBWAY,
            TRAVEL_MODE_BUS,
            TRAVEL_MODE_CHANGE,
            TRAVEL_MODE_SETUP,
            TRAVEL_MODE_DRIVING,
            TRAVEL_MODE_PARKING,
            TRAVEL_MODE_CYCLING
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TravelType {}

    public static final String TRAVEL_MODE_WALKING = "walking";
    public static final String TRAVEL_MODE_SUBWAY = "subway";
    public static final String TRAVEL_MODE_BUS = "bus";
    public static final String TRAVEL_MODE_CHANGE = "change";
    public static final String TRAVEL_MODE_SETUP = "setup";
    public static final String TRAVEL_MODE_DRIVING = "driving";
    public static final String TRAVEL_MODE_PARKING = "parking";
    public static final String TRAVEL_MODE_CYCLING = "cycling";

    @JsonProperty("name")
    private String name;
    @JsonProperty("num_stops")
    private int numOfStops;
    @JsonProperty("travel_mode")
    private String travelMode;
    @JsonProperty("description")
    private String description;
    @JsonProperty("color")
    private String color;
    @JsonProperty("icon_url")
    private String iconUrl;
    @JsonProperty("polyline")
    private String polylineRaw;
    @JsonProperty("stops")
    private List<RouteStop> stops;

    public String getName() {
        return name;
    }

    public int getNumOfStops() {
        return numOfStops;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getPolylineRaw() {
        return polylineRaw;
    }

    public List<RouteStop> getStops() {
        return stops;
    }
}
