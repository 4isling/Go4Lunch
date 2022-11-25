package com.exemple.go4lunch.data.restaurant;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.exemple.go4lunch.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private final RestaurantApi restaurantApi;
    private final String apiKey = String.valueOf(R.string.firebase_web_api_key);
    private final Map<Location, RestaurantResult> alreadyFetchedResponses = new HashMap<>();

    public RestaurantRepository(RestaurantApi restaurantApi){
        this.restaurantApi = restaurantApi;
    }


    public LiveData<List<Result>> getRestaurantLiveData(Location userLocation){
        MutableLiveData<List<Result>> restaurantMutableLiveData = new MutableLiveData<>();

        String sUserLocation = userLocation.toString();

        RestaurantResult restaurantResult = alreadyFetchedResponses.get(sUserLocation);

        if (restaurantResult != null){
            restaurantMutableLiveData.setValue(restaurantResult.getResults());
        }else{
            restaurantApi.getListOfRestaurants("Restaurants", sUserLocation,5000,"restaurant", apiKey)
                    .enqueue(new Callback<RestaurantResult>() {
                @Override
                public void onResponse(Call<RestaurantResult> call, Response<RestaurantResult> response) {
                    if (response.body() != null){
                        alreadyFetchedResponses.put(userLocation, response.body());

                        restaurantMutableLiveData.setValue(response.body().getResults());
                    }
                }

                @Override
                public void onFailure(Call<RestaurantResult> call, Throwable t) {
                    restaurantMutableLiveData.setValue(null);
                }
            });
        }
        return restaurantMutableLiveData;
    }

    private void initData(){

    }
}
