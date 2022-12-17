package com.exemple.go4lunch.data.restaurant;

import android.location.Location;
import android.util.Log;

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
    private List<RestaurantResult> restaurantResultsBackup;
    private MutableLiveData<RestaurantResult> resultMutableLiveData;
    private List<Result> results;

    public RestaurantRepository(RestaurantApi restaurantApi){
        this.restaurantApi = restaurantApi;
    }


    /**
     *
     * @param location
     * @param radius
     * @param type
     * @return
     *
     * le but de la fonction est de faire appel a l'api si le resultat n'as pas deja été demander
     *
     *
     */
    public LiveData<RestaurantResult> getNearbyPlaces(Location location, int radius, String type){
        String sUserLocation = location.toString();
        if (resultMutableLiveData != null) {
            // Si la liste des restaurants est déjà remplie, retournez simplement la liste des restaurants
            return resultMutableLiveData;
        } else {
            // Sinon, faites la requête à l'API et mettez à jour la liste des restaurants
            restaurantApi.getListOfRestaurants(sUserLocation, radius, type).enqueue(new Callback<RestaurantResult>() {
                @Override
                public void onResponse(Call<RestaurantResult> call, Response<RestaurantResult> response) {
                    if (response.isSuccessful()) {
                        results = response.body().getResults();
                        resultMutableLiveData.setValue(response.body());
                    } else {
                        // Gérez les erreurs ici
                        Log.e("restaurant repo","there is an error in getNearbyPlaces");
                    }
                }

                @Override
                public void onFailure(Call<RestaurantResult> call, Throwable t) {
                    // Gérez les erreurs ici
                    Log.e("restaurant repo","there is an error in getNearbyPlaces 2");
                }
            });
        }

        return resultMutableLiveData;
    }



    public LiveData<List<Result>> getRestaurantLiveData(Location userLocation, int radius, String type){
        MutableLiveData<List<Result>> restaurantMutableLiveData = new MutableLiveData<>();

        String sUserLocation = userLocation.toString();

        RestaurantResult restaurantResult = alreadyFetchedResponses.get(sUserLocation);

        if (restaurantResult != null){
            restaurantMutableLiveData.setValue(restaurantResult.getResults());
        }else{
            restaurantApi.getListOfRestaurants(sUserLocation,radius,type)
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
