package com.eutechpro.allytransitapp.map;

import com.eutechpro.allytransitapp.data.model.Route;
import com.google.android.gms.maps.GoogleMap;


public interface RouteDrawer {
    void setMap(GoogleMap map);
    void clearMap();
    void drawRouteOnMap(Route route);
}
