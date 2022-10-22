package com.exemple.go4lunch.model.restaurant;

import androidx.annotation.NonNull;

import com.exemple.go4lunch.data.restaurant.Geometry;
import com.exemple.go4lunch.data.restaurant.OpeningHours;
import com.exemple.go4lunch.data.restaurant.Result;

import java.util.Objects;

public class RestaurantStateItem {

    @NonNull
    private final String name;
    @NonNull
    private final OpeningHours openingHours;
    @NonNull
    private final float rating;
    @NonNull
    private final Geometry geometry;
    @NonNull
    private final String icon;

    public RestaurantStateItem(Result result){
        this.name = result.getName();
        this.rating = result.getRating();
        this.geometry = result.getGeometry();
        this.openingHours = result.getOpeningHours();
        this.icon = result.getIcon();
    }

    public RestaurantStateItem(@NonNull String name, @NonNull OpeningHours openingHours, @NonNull float rating, @NonNull Geometry geometry, @NonNull String icon){
        this.name = name;
        this.openingHours = openingHours;
        this.rating = rating;
        this.geometry = geometry;
        this.icon = icon;
    }

    @NonNull
    public String getName(){
        return name;
    }

    @NonNull
    public OpeningHours getOpeningHours(){
        return openingHours;
    }

    @NonNull
    public float getRating(){
        return rating;
    }

    @NonNull
    public Geometry getGeometry(){
        return geometry;
    }
    @NonNull
    public String getIcon(){
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantStateItem that = (RestaurantStateItem) o;
        return Float.compare(that.rating, rating) == 0 &&
                name.equals(that.name) &&
                openingHours.equals(that.openingHours) &&
                geometry.equals(that.geometry) &&
                icon.equals(that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, openingHours, rating, geometry, icon);
    }
}
