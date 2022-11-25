package com.exemple.go4lunch.ui.workmate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.exemple.go4lunch.data.workmate.WorkmateRepository;
import com.exemple.go4lunch.model.workmate.Workmate;
import com.exemple.go4lunch.model.workmate.WorkmateStateItem;

import java.util.ArrayList;
import java.util.List;

public class WorkmateViewModel extends ViewModel {
    private final WorkmateRepository repository;

    public WorkmateViewModel(){
        repository = WorkmateRepository.getInstance();
    }

    private LiveData<List<WorkmateStateItem>> mapDataToViewState(LiveData<List<Workmate>> workmates){
        return Transformations.map(workmates, workmate ->{
            List<WorkmateStateItem> workmateViewStateItems = new ArrayList<>();
            for (Workmate w : workmate){
                workmateViewStateItems.add(
                        new WorkmateStateItem(w)
                );
            }
            return workmateViewStateItems;
        });
    }

    public LiveData<List<WorkmateStateItem>> getAllWorkmates(){
        return mapDataToViewState(repository.getAllWorkmate());
    }
}