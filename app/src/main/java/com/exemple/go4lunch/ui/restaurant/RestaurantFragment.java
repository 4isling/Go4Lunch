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
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.go4lunch.databinding.FragmentRestaurantBinding;
import com.exemple.go4lunch.ui.ViewModelFactory;

public class RestaurantFragment extends Fragment {

    private FragmentRestaurantBinding binding;
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private RestaurantViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RestaurantViewModel restaurantViewModel =
                new ViewModelProvider(this).get(RestaurantViewModel.class);

        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.initRecyclerView();

        return root;
    }


    private void initRecyclerView(){
        ViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantViewModel.class);
        adapter = new RestaurantAdapter();
        recyclerView.setAdapter(adapter);
        getBaseList();
    }

    private void getBaseList(){
        viewModel.getAllRestaurants().observe(getViewLifecycleOwner(), adapter::submitList);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}