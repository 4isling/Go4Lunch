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
import com.exemple.go4lunch.ui.restaurant.RestaurantListViewModel;
import com.exemple.go4lunch.ui.workmate.WorkmateViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private static volatile ViewModelFactory factory;

    private static Application application;
    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final RestaurantRepository restaurantRepository;
    @NonNull
    private final LocationRepository locationRepository;
    @NonNull
    private final WorkmateRepository workmateRepository;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            application = MainApplication.getApplication();
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
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
                            ),
                            new WorkmateRepository()


                    );
                }
            }
        }
        return factory;
    }

    private ViewModelFactory(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull RestaurantRepository restaurantRepository,
            @NonNull WorkmateRepository workmateRepository

    ) {
        this.restaurantRepository = restaurantRepository;
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;
        this.workmateRepository = workmateRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel();
        }
        if (modelClass.isAssignableFrom(RestaurantListViewModel.class)) {
            return (T) new RestaurantListViewModel(
                    restaurantRepository,
                    locationRepository,
                    permissionChecker
            );
        }
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(
                    permissionChecker,
                    restaurantRepository,
                    locationRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass);
    }
}

