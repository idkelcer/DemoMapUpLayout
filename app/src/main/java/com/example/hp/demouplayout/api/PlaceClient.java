package com.example.hp.demouplayout.api;

import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceClient {

    private Retrofit retrofit;
    private final static String BASE_URL = "http://dev.apimovil.clubelcomercio.pe/api/v1/";

    public PlaceClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public PlaceService getPlaceService() {
        return retrofit.create(PlaceService.class);
    }
}
