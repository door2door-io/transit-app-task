package com.eutechpro.allytransitapp.di;

import com.eutechpro.allytransitapp.data.RetrofitRoutesManager;
import com.eutechpro.allytransitapp.data.RoutesManager;
import com.eutechpro.allytransitapp.data.rest.retrofit.RetrofitClient;

import javax.inject.Singleton;

import dagger.Provides;

public class MockRoutesManagerModule extends RoutesManagerModule {
    private final String url;
    public MockRoutesManagerModule(String url) {
        this.url = url;
    }

    @Singleton
    @Provides
    public RoutesManager provideRoutesManager() {
        return new RetrofitRoutesManager(new RetrofitClient(url));
    }
}
