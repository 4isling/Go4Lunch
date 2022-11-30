package com.exemple.go4lunch.ui.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exemple.go4lunch.databinding.FragmentRestaurantBinding;
import com.exemple.go4lunch.ui.ViewModelFactory;

public class RestaurantFragment extends Fragment {

    private FragmentRestaurantBinding binding;
    private RestaurantAdapter adapter;
    private RestaurantViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initRecyclerView();
        initViewModel();


        return root;
    }


    private void initRecyclerView(){
        adapter = new RestaurantAdapter();
        binding.restaurantList.setAdapter(adapter);
        binding.restaurantList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.restaurantList.setHasFixedSize(true);
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantViewModel.class);
    }

    private void getBaseList(){
        viewModel.getRestaurantsViewStateLiveData().observe(getViewLifecycleOwner(), restaurantsViewState -> {
            adapter.submitList(restaurantsViewState.getRestaurants());
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}