package com.exemple.go4lunch.data;

import androidx.work.impl.Schedulers;

import com.exemple.go4lunch.data.restaurant.RestaurantApi;
import com.exemple.go4lunch.data.restaurant.RestaurantRepository;
import com.exemple.go4lunch.data.restaurant.RestaurantResult;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class Go4LunchStreams {
    private final RestaurantApi restaurantApi;

    public Go4LunchStreams(RestaurantApi restaurantApi){
        this.restaurantApi = restaurantApi;
    }

    public Future<RestaurantResult> streamFetchRestaurant(String restaurant,String location, int radius, String type){
        Executor executor = Executors.newSingleThreadExecutor();
        Callable<RestaurantResult> callable = () ->
                RestaurantApi.getListOfRestaurants(restaurant, location, radius, type).execute().body();
        return executor.submit(callable);
    }

    public  Single<List<RestaurantResult>> streamFetchRestaurantsDetails(String location, int radius, String type) {
        return streamFetchRestaurants(location, radius, type)
                .flatMapIterable((Function<NearbySearch, List<Result>>) NearbySearch::getResults)
                .flatMap((Function<Result, Observable<PlaceDetail>>) result -> streamFetchPlaceDetail(result.getPlaceId()))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers
                        .mainThread());
    }
}
