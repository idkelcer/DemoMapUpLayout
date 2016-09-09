package com.example.hp.demouplayout.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CercaDeMiClient {

    private Retrofit retrofit;
    private final static String BASE_URL = "http://dev.apimovil.clubelcomercio.pe/api/v1/";

    public CercaDeMiClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public CercaDeMiService getPlaceService() {
        return retrofit.create(CercaDeMiService.class);
    }
}
