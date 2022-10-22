package com.exemple.go4lunch.model.workmate;

import androidx.annotation.NonNull;

import java.util.Objects;

public class WorkmateStateItem {
    @NonNull
    private final String avatarUrl;
    @NonNull
    private final String name;

    private String restaurantName;

    private String typeOfFood;

    public WorkmateStateItem(Workmate workmate){
        this.avatarUrl = workmate.getAvatarUrl();
        this.name = workmate.getName();
        this.restaurantName = workmate.getRestaurantName();
        this.typeOfFood = workmate.getPlaceId();
    }

    public WorkmateStateItem(@NonNull String avatarUrl, @NonNull String name, @NonNull String placeId, String restaurantName){
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.typeOfFood = typeOfFood;
        this.restaurantName = restaurantName;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getAvatarUrl() {
        return avatarUrl;
    }
/*
    @NonNull
    public String getPlaceId(){
        return placeId;
    }*/

    @NonNull
    public String getRestaurantName(){
        return restaurantName;
    }

    public String getTypeOfFood(){
        return typeOfFood;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkmateStateItem)) return false;
        WorkmateStateItem workmateStateItem = (WorkmateStateItem) o;
        return  avatarUrl.equals(workmateStateItem.avatarUrl) &&
                name.equals(workmateStateItem.name) &&
                typeOfFood.equals(workmateStateItem.typeOfFood) &&
                restaurantName.equals(workmateStateItem.restaurantName);
    }

    @Override
    public int hashCode(){
        return Objects.hash(avatarUrl, name, typeOfFood, restaurantName);
    }
}
