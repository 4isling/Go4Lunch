package com.exemple.go4lunch.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;


import com.exemple.go4lunch.MainApplication;
import com.exemple.go4lunch.data.FirebaseHelper;
import com.exemple.go4lunch.data.RetrofitService;
import com.exemple.go4lunch.data.location.LocationRepository;
import com.exemple.go4lunch.data.permission_checker.PermissionChecker;
import com.exemple.go4lunch.data.restaurant.RestaurantApi;
import com.exemple.go4lunch.data.restaurant.RestaurantRepository;
import com.exemple.go4lunch.data.workmate.WorkmateRepository;
import com.exemple.go4lunch.ui.map.MapViewModel;
import com.exemple.go4lunch.ui.restaurant.RestaurantViewModel;
import com.exemple.go4lunch.ui.workmate.WorkmateViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory{
    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance(){
        if (factory == null){
            synchronized (ViewModelFactory.class){
                if (factory == null){
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    private final RestaurantRepository restaurantRepository = new RestaurantRepository(
            RetrofitService.getRestaurantApi()
    );

    private final WorkmateRepository workmateRepository = new WorkmateRepository();

    private ViewModelFactory(){ }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        if(modelClass.isAssignableFrom(WorkmateViewModel.class)){
            return (T) new WorkmateViewModel();
        }
        if (modelClass.isAssignableFrom(RestaurantViewModel.class)){
        return (T) new RestaurantViewModel(
                restaurantRepository
        );
        }
        if (modelClass.isAssignableFrom(MapViewModel.class)){
            return (T) new MapViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass);
    }
}

