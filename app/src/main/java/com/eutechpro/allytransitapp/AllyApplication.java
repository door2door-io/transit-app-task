package com.eutechpro.allytransitapp;

import android.app.Application;
import android.content.Context;

import com.eutechpro.allytransitapp.di.DaggerMapsActivityComponent;
import com.eutechpro.allytransitapp.di.DaggerSearchActivityComponent;
import com.eutechpro.allytransitapp.di.MapsActivityComponent;
import com.eutechpro.allytransitapp.di.RouteDrawingModule;
import com.eutechpro.allytransitapp.di.RoutesManagerModule;
import com.eutechpro.allytransitapp.di.SearchActivityComponent;
import com.facebook.stetho.Stetho;

public class AllyApplication extends Application {
    private MapsActivityComponent mapsActivityComponent;
    private SearchActivityComponent searchActivityComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        mapsActivityComponent = DaggerMapsActivityComponent
                .builder()
                .routeDrawingModule(new RouteDrawingModule(getApplicationContext()))
                .build();
        searchActivityComponent = DaggerSearchActivityComponent
                .builder()
                .routesManagerModule(new RoutesManagerModule())
                .build();
    }

    public static MapsActivityComponent MapsActivityComponent(Context context) {
        return ((AllyApplication) context.getApplicationContext()).mapsActivityComponent;
    }

    public void setMapsActivityComponent(MapsActivityComponent mapsActivityComponent) {
        this.mapsActivityComponent = mapsActivityComponent;
    }

    public static SearchActivityComponent SearchActivityComponent(Context context) {
        return ((AllyApplication) context.getApplicationContext()).searchActivityComponent;
    }

    public void setSearchActivityComponent(SearchActivityComponent searchActivityComponent) {
        this.searchActivityComponent = searchActivityComponent;
    }
}
