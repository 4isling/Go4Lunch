package com.exemple.go4lunch.model.workmate;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class Workmate implements Serializable {
    private String id;
    private String avatarUrl;
    private String name;
    private String placeId = "";
    private String restaurantAddress = "";
    private String restaurantName = "";

    public Workmate() {
        // No-argument constructor
    }

    public Workmate(String avatarUrl, String name, String id) {
        this.id = id;
        this.avatarUrl = avatarUrl;
        this.name = name;
    }

    protected Workmate(Parcel parcel){
        id = parcel.readString();
        name = parcel.readString();
        avatarUrl = parcel.readString();
        placeId = parcel.readString();
        restaurantAddress = parcel.readString();
        restaurantName = parcel.readString();
    }

    public static final Parcelable.Creator<Workmate> CREATOR = new Parcelable.Creator<Workmate>() {

        @Override
        public Workmate createFromParcel(Parcel parcel) {
            return new Workmate(parcel);
        }

        @Override
        public Workmate[] newArray(int i) {
            return new Workmate[i];
        }
    };

    //Getter
    public String getId(){
        return id;
    }

    public String getAvatarUrl(){
        return avatarUrl;
    }

    public String getName(){
        return name;
    }

    public String getPlaceId(){
        return placeId;
    }

    public String getRestaurantName(){
        return restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    //Setter


    public void setId(String id) {
        this.id = id;
    }

    public void setAvatarUrl(String avatarUrl){
        this.avatarUrl = avatarUrl;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPlaceId(String placeId){
        this.placeId = placeId;
    }

    public void setRestaurantName(String restaurantName){
        this.restaurantName = restaurantName;
    }

    public void setRestaurantAddress(String restaurantAddress){
        this.restaurantAddress = restaurantAddress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workmate)) return false;
        Workmate workmate = (Workmate) o;
        return  Objects.equals(avatarUrl, workmate.avatarUrl) &&
                Objects.equals(name, workmate.name) &&
                Objects.equals(id, workmate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, avatarUrl, placeId, restaurantName, restaurantAddress);
    }
}
