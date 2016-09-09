package com.example.hp.demouplayout.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PlaceService {

    @Headers("Ecclubsup-api-key: ef1f488234d085c5f15ac5b36623ac89")
    @GET("cercademi")
    Call<PlaceSearchResponse> searchPlaces(@Query("lat") double latitude,
                                           @Query("lng") double longitude,
                                           @Query("km") double distance,
                                           @Query("tipo_beneficio_id") int tipo);

    @Headers("Ecclubsup-api-key: ef1f488234d085c5f15ac5b36623ac89")
    Call<CategoryResponse> getCategories();



}
