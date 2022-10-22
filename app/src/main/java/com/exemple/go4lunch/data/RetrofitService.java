package com.exemple.go4lunch.data;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.exemple.go4lunch.R;
import com.exemple.go4lunch.data.restaurant.RestaurantApi;
import com.exemple.go4lunch.data.restaurant.RestaurantResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.checkerframework.checker.units.qual.C;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Location userLocation;
    private String key = String.valueOf(R.string.firebase_app_key);
    private String baseUrl;

    private static final Gson gson = new GsonBuilder().setLenient().create();
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();


    public void setBaseUrl(Location userLocation, String key){
        this.baseUrl = "\"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+ userLocation + "&radius=5000&type=restaurant&key=" + key + "\"";
    }



}