package com.eutechpro.allytransitapp.data;

import com.eutechpro.allytransitapp.data.model.Provider.ProviderType;
import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.data.model.Route.RouteType;

import java.util.List;

public interface RoutesManager {
    /**
     * Method for getting {@link Route} list.
     *
     * @param dataCallback {@link DataCallback} for getting data after operation completes.
     */
    void getAllRoutes(DataCallback<List<Route>> dataCallback);
    /**
     * Cancel getting all routes.
     */
    void cancelGettingAllRoutes();

    /**
     * Method for getting {@link Route} list filtered by {@link RouteType}
     *
     * @param routeType {@link RouteType} type we want to finter data on.
     * @param dataCallback {@link DataCallback} for getting data after operation completes.
     */
    void getRoutesOfType(@RouteType String routeType, DataCallback<List<Route>> dataCallback);

    /**
     * Cancel getting routes of type.
     */
    void cancelGettingRoutesOfType();

    /**
     * Method for getting {@link Route} list filtered by {@link ProviderType}
     *
     * @param providerType {@link ProviderType} type we want to finter data on.
     * @param dataCallback {@link DataCallback} for getting data after operation completes.
     */
    void getRoutesOfProvider(@ProviderType String providerType, DataCallback<List<Route>> dataCallback);

    /**
     * Cancel getting routes of provider.
     */
    void cancelGettingRoutesOfProvider();

}
