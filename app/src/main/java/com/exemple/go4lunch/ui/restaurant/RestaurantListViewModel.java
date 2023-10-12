package com.exemple.go4lunch.ui.restaurant;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.exemple.go4lunch.data.location.LocationRepository;
import com.exemple.go4lunch.data.permission_checker.PermissionChecker;
import com.exemple.go4lunch.data.restaurant.RestaurantRepository;
import com.exemple.go4lunch.data.restaurant.nerbysearch.RestaurantResult;
import com.exemple.go4lunch.data.restaurant.nerbysearch.Result;
import com.exemple.go4lunch.ui.map.RestaurantMapViewState;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.List;

public class RestaurantListViewModel extends ViewModel {
    private final static String TAG = "RestaurantListViewModel";

    @NonNull
    private final PermissionChecker permissionChecker;

    @NonNull
    private final RestaurantRepository restaurantRepository;

    private final Location defaultLocation = new Location("48.888053, 2.343312");

    @NonNull
    private final LocationRepository locationRepository;

    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    private final  LiveData<Location> locationLiveData;

    private final MediatorLiveData<Location> locationMutableLiveData = new MediatorLiveData<>();

    private LiveData<RestaurantsListViewState> restaurantsViewStateLiveData;

    private LiveData<RestaurantResult> restaurantResultLiveData;


    public RestaurantListViewModel(@NonNull RestaurantRepository restaurantRepository,
                                   @NonNull LocationRepository locationRepository,
                                   @NonNull PermissionChecker permissionChecker
    ){
        this.restaurantRepository = restaurantRepository;
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;

        locationLiveData = locationRepository.getLocationLiveData();

        locationMutableLiveData.addSource(locationLiveData, location ->
                combine(location, hasGpsPermissionLiveData.getValue()));

        locationMutableLiveData.addSource(hasGpsPermissionLiveData, hasGpsPermissionLiveData ->
                combine(locationLiveData.getValue(), hasGpsPermissionLiveData));

        if(locationMutableLiveData.getValue() != null) {
            restaurantsViewStateLiveData = Transformations.switchMap(locationLiveData, userLocation ->
                    Transformations.map(this.restaurantRepository.getNearbyPlaces(userLocation, 5000, "restaurant"), restaurantResult ->
                            mapDataToViewState(restaurantResult.getResults()))
            );
        }
    }

    private void combine(@NonNull Location location, @Nullable Boolean hasGpsPermission) {
        if (location == null){
            if (hasGpsPermission == null || !hasGpsPermission){
                locationMutableLiveData.setValue(defaultLocation);
                Log.e(TAG , ": combine: No gps permission");
            } else {
                locationMutableLiveData.setValue(defaultLocation);
                Log.d(TAG, ": combine:  Querying location,Wait for location");
            }
        }else{
            locationMutableLiveData.setValue(location);
            Log.i(TAG, ": combine: the current location is: lat: " + location.getLatitude() + " lng: "+ location.getLongitude());
        }
    }

    public void setRestaurantList(){

    }

    public LiveData<RestaurantsListViewState> getRestaurantsViewStateLiveData() {
        return restaurantsViewStateLiveData;
    }

    private RestaurantsListViewState mapDataToViewState(@Nullable List<Result> restaurants) {
        List<Result> restaurantToBeDisplayed = new ArrayList<>();

        if (restaurants != null){
            restaurantToBeDisplayed.addAll(restaurants);
        }
        return new RestaurantsListViewState(
                restaurantToBeDisplayed
        );
    }



    @SuppressLint("MissingPermission")
    public void refresh(){
        boolean hasGpsPermission = permissionChecker.hasLocationPermission();
        hasGpsPermissionLiveData.setValue(hasGpsPermission);

        if(hasGpsPermission){
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
    }
}