package com.eutechpro.allytransitapp.di;

import android.content.Context;

import com.eutechpro.allytransitapp.map.RouteDrawingI;
import com.eutechpro.allytransitapp.map.RouteDrawingManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RouteDrawingModule {
    private Context context;

    public RouteDrawingModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public RouteDrawingI provideRouteDrawingManager(){
        return new RouteDrawingManager(context);
    }
}
