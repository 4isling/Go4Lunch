package com.exemple.go4lunch.ui.restaurant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.exemple.go4lunch.data.restaurant.RestaurantRepository;
import com.exemple.go4lunch.model.restaurant.Restaurant;
import com.exemple.go4lunch.model.restaurant.RestaurantStateItem;

import java.util.ArrayList;
import java.util.List;

public class RestaurantViewModel extends ViewModel {
    private final RestaurantRepository repository;
    private final MutableLiveData<> currentMutableLiveData = new MutableLiveData();
    public RestaurantViewModel(RestaurantRepository restaurantRepository) {
        repository = restaurantRepository;

        current
    }

    private LiveData<List<RestaurantStateItem>> mapDataToViewState(LiveData<List<Restaurant>> restaurants) {
        return Transformations.map(restaurants, restaurant -> {
            List<RestaurantStateItem> restaurantViewStateItems = new ArrayList<>();
            for (Restaurant r : restaurant) {
                restaurantViewStateItems.add(
                        new RestaurantStateItem(r)
                );
            }
            return restaurantViewStateItems;
        });
    }

    public LiveData<List<RestaurantStateItem>> getAllRestaurants(){
        return mapDataToViewState(repository.getRestaurantLiveData());
    }
}