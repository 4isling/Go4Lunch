package com.exemple.go4lunch.model.restaurant;

import android.os.Parcel;

public class Restaurant {
    private String name;
    private String photoReference; // inside data/restaurant/Photo
    private String address;
    private String typeOfFood;
    private String date;
    private int rating;

    public Restaurant(String name, String photoReference, String address, String typeOfFood, String date, int rating){
        this.name = name;
        this.photoReference =photoReference;
        this.address = address;
        this.typeOfFood = typeOfFood;
        this.date = date;
        this.rating = rating;
    }

    protected Restaurant(Parcel parcel){
        name = parcel.readString();
        photoReference = parcel.readString();
        address = parcel.readString();
        typeOfFood = parcel.readString();
        date = parcel.readString();
        rating = parcel.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypeOfFood() {
        return typeOfFood;
    }

    public void setTypeOfFood(String typeOfFood) {
        this.typeOfFood = typeOfFood;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}