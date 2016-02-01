package com.eutechpro.allytransitapp.di;

import com.eutechpro.allytransitapp.map.MapsActivity;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = {RouteDrawingModule.class})
public interface MapsActivityComponent {
    void inject(MapsActivity mapsActivity);
}
