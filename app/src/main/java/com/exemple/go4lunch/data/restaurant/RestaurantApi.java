package com.exemple.go4lunch.data.restaurant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//doc place pour l'appel

public interface RestaurantApi {

    @GET("nearbysearch/json")
    Call<RestaurantResult> getListOfRestaurants(@Query("keyword")String restaurant,
                                                @Query("location")String location,
                                                @Query("radius")int radius,
                                                @Query("type")String type,
                                                @Query("key")String key
                                                );



}
