package com.eutechpro.allytransitapp.di;

import com.eutechpro.allytransitapp.search.SearchRouteActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RoutesManagerModule.class})
public interface SearchActivityComponent {
    void inject(SearchRouteActivity searchRouteActivity);
}
