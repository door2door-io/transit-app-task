package com.eutechpro.allytransitapp.data.rest.retrofit;

import android.support.annotation.NonNull;

import com.eutechpro.allytransitapp.data.DataCallback;
import com.eutechpro.allytransitapp.data.DataError;
import com.eutechpro.allytransitapp.data.model.Route;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class RetrofitClient implements RetrofitAPI {
    private static final String BASE_URL = "http://www.mocky.io/v2/";
    public static final int HTTP_UNKNOWN_ERROR = 520;
    public static final int READ_TIMEOUT = 10;
    public static final int CONNECTION_TIMEOUT = 10;
    private RetrofitRestInterface retrofitRestInterface;

    public RetrofitClient(@NonNull String url){
        OkHttpClient client = setupOKHttpClient();
        buildApiServices(client, url);
    }
    public RetrofitClient() {
        OkHttpClient client = setupOKHttpClient();
        buildApiServices(client, BASE_URL);
    }

    @NonNull
    private OkHttpClient setupOKHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        return client;
    }

    private void buildApiServices(OkHttpClient client, String url) {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        om.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(RoutesResponse.class, new RoutesDeserializer());
        om.registerModule(module);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(om))
                .baseUrl(url)
                .client(client)
                .build();
        retrofitRestInterface = retrofit.create(RetrofitRestInterface.class);
    }

    @Override
    public Call<RoutesResponse> fetchRoutes(final DataCallback<List<Route>> dataCallback) {
        dataCallback.beforeStart();
        Call<RoutesResponse> call = retrofitRestInterface.getRoutes();
        call.enqueue(new Callback<RoutesResponse>() {
            @Override
            public void onResponse(Response<RoutesResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    dataCallback.onResponse(response.body().routes);
                } else {
                    //TODO process error on speciffic way, depending on potential error response.
                    DataError dataError = new DataError(520, "Fetching of routes failed in unknown reason.");
                    dataCallback.onFailure(dataError);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                DataError dataError = new DataError(HTTP_UNKNOWN_ERROR, t.getLocalizedMessage());
                dataCallback.onFailure(dataError);
            }
        });
        return call;
    }
}
