package com.exemple.go4lunch.data.restaurant;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private final RestaurantApi restaurantApi;

    private final Map<Integer, RestaurantResult> alreadyFetchedResponses = new HashMap<>();

    public RestaurantRepository(RestaurantApi restaurantApi){
        this.restaurantApi = restaurantApi;
    }

    public LiveData<List<Result>> getRestaurantLiveData(Location userLocation){
        MutableLiveData<List<Result>> restaurantMutableLiveData = new MutableLiveData<>();

        RestaurantResult result = alreadyFetchedResponses.get(userLocation);

        if (result != null){
            restaurantMutableLiveData.setValue(result.getResults());
        }else{
            restaurantApi.getListOfRestaurants("Restaurants", userLocation,5000,"restaurant", apiKey).enqueue(new Callback<RestaurantResult>() {
                @Override
                public void onResponse(Call<RestaurantResult> call, Response<RestaurantResult> response) {
                    if (response.body() != null){
                        alreadyFetchedResponses.put(page, response.body());

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
}
