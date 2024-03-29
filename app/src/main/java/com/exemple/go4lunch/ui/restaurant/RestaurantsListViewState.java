package com.exemple.go4lunch.ui.restaurant;

import androidx.annotation.NonNull;

import com.exemple.go4lunch.data.restaurant.nerbysearch.Result;

import java.util.List;
import java.util.Objects;

public class RestaurantsListViewState {
    @NonNull
    private final List<Result> restaurants;

    public RestaurantsListViewState(
            @NonNull List<Result> restaurants
    ){
        this.restaurants = restaurants;
    }

    @NonNull
    public List<Result> getRestaurants(){
        return restaurants;
    }

    @Override
    public int hashCode(){
        return Objects.hash(restaurants);
    }

}
