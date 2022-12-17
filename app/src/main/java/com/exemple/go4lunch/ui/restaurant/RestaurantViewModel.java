package com.exemple.go4lunch.ui.restaurant;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.exemple.go4lunch.data.location.LocationRepository;
import com.exemple.go4lunch.data.permission_checker.PermissionChecker;
import com.exemple.go4lunch.data.restaurant.RestaurantRepository;
import com.exemple.go4lunch.data.restaurant.RestaurantResult;
import com.exemple.go4lunch.data.restaurant.Result;


import java.util.ArrayList;
import java.util.List;

public class RestaurantViewModel extends ViewModel {

    @NonNull
    private final PermissionChecker permissionChecker;

    @NonNull
    private final RestaurantRepository restaurantRepository;

    @NonNull
    private final LocationRepository locationRepository;

    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    private final LiveData<Location> locationLiveData;

    private LiveData<RestaurantsViewState> restaurantsViewStateLiveData;

    private LiveData<List<RestaurantResult>> restaurantResultLiveData;

    public RestaurantViewModel(@NonNull RestaurantRepository restaurantRepository,
                               @NonNull LocationRepository locationRepository,
                               @NonNull PermissionChecker permissionChecker
    ){
        this.restaurantRepository = restaurantRepository;
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;

        this.locationLiveData = locationRepository.getLocationLiveData();

    /*    restaurantsViewStateLiveData = Transformations.switchMap(locationLiveData ,userLocation ->
                Transformations.map(restaurantRepository.getRestaurantLiveData(userLocation, 5000, type), this::mapDataToViewState)
        );*/
    }

    public LiveData<List<Result>> getRestaurantsList(Location location, int radius, String type){
        restaurantResultLiveData = restaurantRepository.getNearbyPlaces(location, radius, type);

        return restaurantResultLiveData;
    }

    public LiveData<RestaurantsViewState> getRestaurantsViewStateLiveData() {
        return restaurantsViewStateLiveData;
    }

    private RestaurantsViewState mapDataToViewState(@Nullable List<Result> restaurants) {
        List<Result> restaurantToBeDisplayed = new ArrayList<>();

        if (restaurants != null){
            restaurantToBeDisplayed.addAll(restaurants);
        }
        return new RestaurantsViewState(
                restaurantToBeDisplayed
        );
    }

    /*
    private LiveData<List<RestaurantStateItem>> mapDataToViewState(@Nullable LiveData<List<Result>> restaurants) {
        return Transformations.map(restaurants, restaurant -> {
            List<RestaurantStateItem> restaurantViewStateItems = new ArrayList<>();
            for (Result r : restaurant) {
                restaurantViewStateItems.add(
                        new RestaurantStateItem(r)
                );
            }
            return restaurantViewStateItems;
        });
    }*/

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