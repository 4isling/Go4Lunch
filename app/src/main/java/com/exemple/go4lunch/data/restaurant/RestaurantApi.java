package com.exemple.go4lunch.data.restaurant;

import com.exemple.go4lunch.BuildConfig;
import com.exemple.go4lunch.data.restaurant.detail.DetailResponse;
import com.exemple.go4lunch.data.restaurant.nerbysearch.RestaurantResult;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//doc place pour l'appel

public interface RestaurantApi {
    String API_KEY = BuildConfig.MAPS_API_KEY;
    @GET("nearbysearch/json?key=" + API_KEY)
    Call<RestaurantResult> getListOfRestaurants(@Query("location") String location,
                                                      @Query("radius") int radius,
                                                      @Query("type") String type
    );

    @GET("details/json?key=" + API_KEY)
    Call<DetailResponse> getPlaceDetails(@Query("place_id")String placeId);



}
