package com.exemple.go4lunch.data.restaurant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantApi {
    @GET("restaurants")
    Call<RestaurantResult> getListOfRestaurants(@Query("results")int result);
}
