package com.exemple.go4lunch.ui.restaurant;

import androidx.annotation.NonNull;

import com.exemple.go4lunch.data.restaurant.Result;

import java.util.List;
import java.util.Objects;

public class RestaurantsViewState {
    @NonNull
    private final List<Result> restaurants;

    private final boolean isPreviousPageButtonEnabled;

    public RestaurantsViewState(
            @NonNull List<Result> restaurants,
            boolean isPreviousPageButtonEnabled
    ){
        this.restaurants = restaurants;
        this.isPreviousPageButtonEnabled = isPreviousPageButtonEnabled;
    }
    @NonNull
    public List<Result> getRestaurants(){
        return restaurants;
    }

    public boolean isPreviousPageButtonEnabled(){
        return isPreviousPageButtonEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantsViewState that = (RestaurantsViewState) o;
        return isPreviousPageButtonEnabled == that.isPreviousPageButtonEnabled &&
                restaurants.equals(that.restaurants);
    }

    @Override
    public int hashCode(){
        return Objects.hash(restaurants, isPreviousPageButtonEnabled);
    }

    @NonNull
    @Override
    public String toString(){
        return "RestaurantsViewState{"+
                "restaurants"+ restaurants +
                ", isPreviousPageButtonEnabled=" + isPreviousPageButtonEnabled +
                '}';
    }
}
