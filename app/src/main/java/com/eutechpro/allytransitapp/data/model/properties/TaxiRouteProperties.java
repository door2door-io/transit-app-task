package com.eutechpro.allytransitapp.data.model.properties;

import com.eutechpro.allytransitapp.data.model.TaxiCompany;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import java.util.List;


@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class TaxiRouteProperties {
    private List<TaxiCompany> companies;
}
