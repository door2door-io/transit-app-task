package com.eutechpro.allytransitapp.data.rest.retrofit;

import com.eutechpro.allytransitapp.data.DataCallback;
import com.eutechpro.allytransitapp.data.model.Route;

import java.util.List;

import retrofit.Call;

/**
 * Created by Kursulla on 12/01/16.
 */
public interface RetrofitAPI {
    Call<RoutesResponse> fetchRoutes(DataCallback<List<Route>> dataCallback);
}
