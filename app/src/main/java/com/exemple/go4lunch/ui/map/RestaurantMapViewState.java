package com.exemple.go4lunch.ui.map;

import androidx.annotation.NonNull;

import com.exemple.go4lunch.data.restaurant.nerbysearch.Geometry;
import com.exemple.go4lunch.data.restaurant.nerbysearch.Location;
import com.exemple.go4lunch.data.restaurant.nerbysearch.Result;

import java.util.List;
import java.util.Objects;

public class RestaurantMapViewState {

    @NonNull
    private final List<Result> restaurants;

    private List<Location> restaurantLocation;
    private android.location.Location currentLocation;

    public RestaurantMapViewState(
            @NonNull List<Result> restaurants,
            android.location.Location userLocation
    ){
        this.restaurants = restaurants;
        setRestaurantLocation();
        setCurrentLocation(userLocation);
    }

    @NonNull
    public List<Result> getRestaurantMapResult(){
        return restaurants;
    }

    private List<Location> setRestaurantLocation(){
        for (Result restaurant: restaurants) {
            restaurantLocation.add(restaurant.getGeometry().getLocation());
        }
        return restaurantLocation;
    }

    private void setCurrentLocation(android.location.Location userLocation){
        this.currentLocation = userLocation;
    }

    public android.location.Location getCurrentLocation(){
        return currentLocation;
    }

    public List<Location> getRestaurantsLocations() {
        return restaurantLocation;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        RestaurantMapViewState that = (RestaurantMapViewState) o;
        return restaurants.equals(that.restaurants);
    }

    @Override
    public int hashCode(){
        return Objects.hash(restaurants);
    }

    @NonNull
    @Override
    public String toString(){
        return "RestaurantMapViewState{" +
                "restaurants=" + restaurants+'}';
    }
}
