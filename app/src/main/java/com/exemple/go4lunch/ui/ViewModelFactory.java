package com.exemple.go4lunch.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;


import com.exemple.go4lunch.MainApplication;
import com.exemple.go4lunch.data.RetrofitService;
import com.exemple.go4lunch.data.location.LocationRepository;
import com.exemple.go4lunch.data.permission_checker.PermissionChecker;
import com.exemple.go4lunch.data.restaurant.RestaurantRepository;
import com.exemple.go4lunch.data.workmate.WorkmateRepository;
import com.exemple.go4lunch.ui.map.MapViewModel;
import com.exemple.go4lunch.ui.restaurant.RestaurantViewModel;
import com.exemple.go4lunch.ui.workmate.WorkmateViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private static volatile ViewModelFactory factory;

    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final RestaurantRepository restaurantRepository;
    @NonNull
    private final LocationRepository locationRepository;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    Application application = MainApplication.getApplication();
                    factory = new ViewModelFactory(
                            new PermissionChecker(
                                    application
                            ),
                            new LocationRepository(
                                    LocationServices.getFusedLocationProviderClient(
                                            application
                                    )
                            ),
                            new RestaurantRepository(
                                    RetrofitService.getRestaurantApi()
                            )
                    );
                }
            }
        }
        return factory;
    }

    private final WorkmateRepository workmateRepository = new WorkmateRepository();

    private ViewModelFactory(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull RestaurantRepository restaurantRepository
    ) {
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;
        this.restaurantRepository = restaurantRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel();
        }
        if (modelClass.isAssignableFrom(RestaurantViewModel.class)) {
            return (T) new RestaurantViewModel(
                    restaurantRepository,
                    locationRepository,
                    permissionChecker
            );
        }
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass);
    }
}

