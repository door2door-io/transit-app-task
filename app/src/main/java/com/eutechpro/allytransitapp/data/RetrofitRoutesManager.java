package com.eutechpro.allytransitapp.data;

import android.support.annotation.NonNull;

import com.eutechpro.allytransitapp.data.rest.retrofit.RetrofitClient;
import com.eutechpro.allytransitapp.data.rest.retrofit.RoutesResponse;
import com.eutechpro.allytransitapp.data.model.Provider.ProviderType;
import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.data.model.Route.RouteType;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;

/**
 * In this implementation, {@link List<Route>} will be fetched from the internet with {@link RetrofitClient}.
 */
public class RetrofitRoutesManager implements RoutesManager {
    private static Route routeAsArgument;
    private RetrofitClient retrofitClient;
    private Call<RoutesResponse> getAllRoutesCall;
    private Call<RoutesResponse> getRoutesOfTypeCall;
    private Call<RoutesResponse> getRoutesOfProviderCall;

    public RetrofitRoutesManager(RetrofitClient retrofitClient) {
        this.retrofitClient = retrofitClient;
    }

    @Override
    public void getAllRoutes(final DataCallback<List<Route>> dataCallback) {
        getAllRoutesCall = retrofitClient.fetchRoutes(new DataCallback<List<Route>>() {
            @Override
            public void beforeStart() {
                dataCallback.beforeStart();
            }

            @Override
            public void onResponse(List<Route> routes) {
                dataCallback.onResponse(routes);
            }

            @Override
            public void onFailure(DataError dataError) {
                dataCallback.onFailure(dataError);
            }
        });
    }

    @Override
    public void cancelGettingAllRoutes() {
        if (getAllRoutesCall != null) {
            getAllRoutesCall.cancel();
        }
    }
    @Override
    public void getRoutesOfType(@RouteType final String routeType, final DataCallback<List<Route>> dataCallback) {
        getRoutesOfTypeCall = retrofitClient.fetchRoutes(new DataCallback<List<Route>>() {
            @Override
            public void beforeStart() {
                dataCallback.beforeStart();
            }

            @Override
            public void onResponse(List<Route> routes) {
                dataCallback.onResponse(filterRoutesByType(routeType, routes));
            }

            @Override
            public void onFailure(DataError dataError) {
                dataCallback.onFailure(dataError);
            }
        });
    }

    @Override
    public void cancelGettingRoutesOfType() {
        if(getRoutesOfTypeCall != null){
            getRoutesOfTypeCall.cancel();
        }
    }

    @Override
    public void getRoutesOfProvider(@ProviderType final String providerType, final DataCallback<List<Route>> dataCallback) {
        getRoutesOfProviderCall = retrofitClient.fetchRoutes(new DataCallback<List<Route>>() {
            @Override
            public void beforeStart() {
                dataCallback.beforeStart();
            }

            @Override
            public void onResponse(List<Route> routes) {
                dataCallback.onResponse(filterRoutesByProviderType(providerType, routes));
            }

            @Override
            public void onFailure(DataError dataError) {
                dataCallback.onFailure(dataError);
            }
        });
    }

    @Override
    public void cancelGettingRoutesOfProvider() {
        if(getRoutesOfProviderCall != null){
            getRoutesOfProviderCall.cancel();
        }
    }

    public static Route getRouteAsArgument() {
        return routeAsArgument;
    }

    public static void setRouteAsArgument(Route routeAsArgument) {
        RetrofitRoutesManager.routeAsArgument = routeAsArgument;
    }

    @NonNull
    private List<Route> filterRoutesByType(@RouteType String routeType, List<Route> routes) {
        List<Route> routesOfType = new ArrayList<>();
        for (Route route : routes) {
            if (route.getType().equals(routeType)) {
                routesOfType.add(route);
            }
        }
        return routesOfType;
    }

    @NonNull
    private List<Route> filterRoutesByProviderType(@ProviderType String providerType, List<Route> routes) {
        List<Route> routesOfType = new ArrayList<>();
        for (Route route : routes) {
            if (route.getProvider().getProviderType().equals(providerType)) {
                routesOfType.add(route);
            }
        }
        return routesOfType;
    }

}
