package com.eutechpro.allytransitapp.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Price {
    @JsonProperty("currency")
    private String currency;
    private int amount;

    public String getCurrency() {
        return currency;
    }

    public int getAmount() {
        return amount;
    }
}
