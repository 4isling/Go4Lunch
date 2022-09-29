package com.exemple.go4lunch.ui.workmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.exemple.go4lunch.databinding.FragmentWorkmateBinding;

public class WorkmateFragment extends Fragment {

    private FragmentWorkmateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkmateViewModel workmateViewModel =
                new ViewModelProvider(this).get(WorkmateViewModel.class);

        binding = FragmentWorkmateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textWorkmate;
        workmateViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}