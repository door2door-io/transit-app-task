package com.eutechpro.allytransitapp.search;

import com.eutechpro.allytransitapp.data.DataCallback;
import com.eutechpro.allytransitapp.data.DataError;
import com.eutechpro.allytransitapp.data.RoutesManager;
import com.eutechpro.allytransitapp.data.model.Route;

import java.util.List;

/**
 * Created by Kursulla on 12/02/16.
 */
public class SearchRouteActivityPresenter {
    private SearchRouteView targetView;
    private RoutesManager routesManager;

    public SearchRouteActivityPresenter(SearchRouteView targetView) {
        this.targetView = targetView;
    }

    public void addDataRepository(RoutesManager routesManager) {
        this.routesManager = routesManager;
    }

    public void stopEverything() {
        routesManager.cancelGettingAllRoutes();
    }

    public void performSearch() {
        routesManager.getAllRoutes(new DataCallback<List<Route>>() {
            @Override
            public void beforeStart() {

            }

            @Override
            public void onResponse(List<Route> fetchedRoutes) {
                targetView.searchSuccessfull(fetchedRoutes);
            }

            @Override
            public void onFailure(DataError dataError) {
                targetView.searchFailed(dataError);
            }
        });
    }

}
