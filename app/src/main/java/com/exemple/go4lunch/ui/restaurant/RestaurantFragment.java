package com.exemple.go4lunch.ui.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.exemple.go4lunch.databinding.FragmentRestaurantBinding;

public class RestaurantFragment extends Fragment {

    private FragmentRestaurantBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RestaurantViewModel restaurantViewModel =
                new ViewModelProvider(this).get(RestaurantViewModel.class);

        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textRestaurant;
        restaurantViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}