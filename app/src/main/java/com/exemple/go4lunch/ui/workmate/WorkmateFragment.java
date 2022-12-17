package com.exemple.go4lunch.ui.workmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exemple.go4lunch.R;
import com.exemple.go4lunch.databinding.FragmentWorkmateBinding;
import com.exemple.go4lunch.ui.ViewModelFactory;
import com.exemple.go4lunch.ui.restaurant.RestaurantViewModel;

public class WorkmateFragment extends Fragment {
    private WorkmateViewModel workmateViewModel;
    private RestaurantViewModel restaurantViewModel;
    private WorkmateAdapter adapter;
    private RecyclerView recyclerView;
    private FragmentWorkmateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workmateViewModel = new ViewModelProvider(this).get(WorkmateViewModel.class);
        //restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        binding = FragmentWorkmateBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        initRecyclerView();
        return root;
    }

    /**
     * initialise the recyclerView with binding a
     */
    private void initRecyclerView(){
        recyclerView = binding.workmateList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new WorkmateAdapter();
        recyclerView.setAdapter(adapter);
        getBaseList();
    }

    private void getBaseList() {
        workmateViewModel.getAllWorkmates().observe(getViewLifecycleOwner(), workmates -> adapter.setList(workmates));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}