package com.eutechpro.allytransitapp.data.rest.retrofit;

import retrofit.Call;
import retrofit.http.GET;

/**
 * REST API interface for communicating with server.
 */
public interface RetrofitRestInterface {
    @GET("56abe50d0f0000ed3e987f48")
    Call<RoutesResponse> getRoutes();
}
