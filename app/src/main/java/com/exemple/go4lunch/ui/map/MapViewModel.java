package com.exemple.go4lunch.ui.map;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.exemple.go4lunch.MainApplication;
import com.exemple.go4lunch.data.RetrofitService;
import com.exemple.go4lunch.data.location.LocationRepository;
import com.exemple.go4lunch.data.permission_checker.PermissionChecker;
import com.exemple.go4lunch.data.restaurant.RestaurantApi;
import com.exemple.go4lunch.data.restaurant.RestaurantRepository;
import com.exemple.go4lunch.data.restaurant.nerbysearch.RestaurantResult;
import com.exemple.go4lunch.data.restaurant.nerbysearch.Result;
import com.exemple.go4lunch.data.workmate.WorkmateRepository;
import com.exemple.go4lunch.ui.workmate.WorkmateViewModel;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewModel extends ViewModel {
    @NonNull
    private final LocationRepository locationRepository;
    @NonNull
    private final RestaurantRepository restaurantRepository;

    private final LiveData<Location> currentLocationLiveData;

    private final LiveData<RestaurantMapViewState> restaurantMapViewStateLiveData;

    public MapViewModel(@NonNull PermissionChecker permissionChecker,
                        @NonNull RestaurantRepository restaurantRepository,
                        @NonNull LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;

        currentLocationLiveData = locationRepository.getLocationLiveData();

        //if the LiveData that contains the current user location information change
        restaurantMapViewStateLiveData  = Transformations.switchMap(currentLocationLiveData, currentLocation ->
                // query the repository to get the user location (with a Transformations.switchMap)
                Transformations.map(restaurantRepository.getNearbyPlaces(currentLocation, 5000, "restaurant"), restaurants ->
                        mapDataToViewState(restaurants.getResults(), currentLocation)
                )
        );
    }

    private RestaurantMapViewState mapDataToViewState(@Nullable List<Result> restaurants, Location currentLocation) {
        List<Result> restaurantToBeDisplayed = new ArrayList<>();

        if (restaurants != null){
                restaurantToBeDisplayed.addAll(restaurants);
        }

        return new RestaurantMapViewState(restaurantToBeDisplayed, currentLocation);
    }


    //Product to be call from the View
    public LiveData<RestaurantMapViewState> getRestaurantMapViewStateLiveData(){
        return restaurantMapViewStateLiveData;
    }

}
