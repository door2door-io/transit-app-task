package com.eutechpro.allytransitapp.data.rest.retrofit;

import com.eutechpro.allytransitapp.data.model.Route;

import java.util.List;

/**
 * Wrapper class for Routes response.
 * <br/>
 * It is good practice to keep response in wrapper for potential adding additional attributes.
 * <br/>
 * For example pagination data(total, offset, limit...).
 */
public class RoutesResponse {
    public List<Route> routes;
}
