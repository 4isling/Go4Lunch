package com.exemple.go4lunch.ui.restaurant;

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
import com.exemple.go4lunch.data.restaurant.Result;


import java.util.ArrayList;
import java.util.List;

public class RestaurantViewModel extends ViewModel {

    private final RestaurantRepository repository;

    private final MutableLiveData<Location> currentUserLocation = new MutableLiveData<>();

    private final LiveData<RestaurantsViewState> restaurantsViewStateLiveData;

    public RestaurantViewModel(RestaurantRepository restaurantRepository) {
        repository = restaurantRepository;

        restaurantsViewStateLiveData = Transformations.switchMap(currentUserLocation ,userLocation ->
                Transformations.map(repository.getRestaurantLiveData(userLocation), this::mapDataToViewState)
        );
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
}