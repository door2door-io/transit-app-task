package com.eutechpro.allytransitapp.data.model.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class BikeSharingRouteProperties {
    private String id;
    @JsonProperty("available_bikes")
    private int availableBikes;
}
