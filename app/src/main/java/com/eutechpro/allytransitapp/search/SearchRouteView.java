package com.eutechpro.allytransitapp.search;

import com.eutechpro.allytransitapp.data.DataError;
import com.eutechpro.allytransitapp.data.model.Route;

import java.util.List;

/**
 * Created by Kursulla on 14/02/16.
 */
public interface SearchRouteView {
    void searchSuccessfull(List<Route> fetchedRoutes);
    void searchFailed(DataError dataError);
    void routeSelected(Route selectedRoute);
}
