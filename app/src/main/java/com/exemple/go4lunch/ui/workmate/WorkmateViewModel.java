package com.exemple.go4lunch.ui.workmate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkmateViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WorkmateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}