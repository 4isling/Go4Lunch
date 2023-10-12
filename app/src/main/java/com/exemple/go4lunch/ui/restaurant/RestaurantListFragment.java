package com.exemple.go4lunch.ui.restaurant;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.exemple.go4lunch.data.permission_checker.PermissionChecker;
import com.exemple.go4lunch.databinding.FragmentRestaurantBinding;
import com.exemple.go4lunch.ui.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class RestaurantListFragment extends Fragment {
    private static final String TAG = "RestaurantFragment";
    int REQ_PERMISSION = 100;
    private FragmentRestaurantBinding binding;
    private RestaurantAdapter adapter;
    private RestaurantListViewModel viewModel;


    public FusedLocationProviderClient fusedLocationProviderClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setFusedLocationProviderClient();
        initViewModel();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        if(new PermissionChecker(getActivity().getApplication()).hasLocationPermission()) {
            initRecyclerView();
            //    getBaseList();
        }else{
            Log.d(TAG, "no location permission");
            getLocationPermission();
        }
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantListViewModel.class);
    }

    private void initRecyclerView(){
        adapter = new RestaurantAdapter();
        binding.restaurantList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.restaurantList.setAdapter(adapter);
        binding.restaurantList.setHasFixedSize(true);
        viewModel.getRestaurantsViewStateLiveData().observe(getViewLifecycleOwner(), restaurantsListViewState -> {
            adapter.submitList(restaurantsListViewState.getRestaurants());
        });
    }

    private void setFusedLocationProviderClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    private void getLocationPermission() {
        //request location permission
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(viewModel!= null) {
            viewModel.refresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}