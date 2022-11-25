package com.exemple.go4lunch.ui.workmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.exemple.go4lunch.databinding.FragmentWorkmateBinding;
import com.exemple.go4lunch.ui.ViewModelFactory;

public class WorkmateFragment extends Fragment {
    private WorkmateViewModel viewModel;
    private WorkmateAdapter adapter;

    private FragmentWorkmateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkmateViewModel workmateViewModel =
                new ViewModelProvider(this).get(WorkmateViewModel.class);

        binding = FragmentWorkmateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initRecyclerView();
        return root;
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = binding.workmateList;
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(WorkmateViewModel.class);
        adapter = new WorkmateAdapter();
        recyclerView.setAdapter(adapter);
    }
/*
    private void getBaseList(){
        viewModel.getAllWorkmates().observe(this.adapter::submitList);
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}