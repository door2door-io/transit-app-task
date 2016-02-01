package com.eutechpro.allytransitapp.di;

import com.eutechpro.allytransitapp.data.RetrofitRoutesManager;
import com.eutechpro.allytransitapp.data.RoutesManager;
import com.eutechpro.allytransitapp.data.rest.retrofit.RetrofitClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoutesManagerModule {
    @Singleton
    @Provides
    public RoutesManager provideRoutesManager(){
        return new RetrofitRoutesManager(new RetrofitClient());
    }
}
