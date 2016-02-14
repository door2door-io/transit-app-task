package com.eutechpro.allytransitapp.data;

/**
 * Callback that will be used in Application layer to work with data layer.
 * <br/>
 * If we replace Retrofit with something else, communication with data layer will keep it's interface.
 *
 * @param <T> Class that represents usable object
 */
public interface DataCallback<T> {
    void beforeStart();
    void onResponse(T dataObject);
    void onFailure(DataError dataError);
}
