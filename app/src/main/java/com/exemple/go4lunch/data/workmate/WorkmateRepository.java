package com.exemple.go4lunch.data.workmate;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.exemple.go4lunch.data.FirebaseHelper;
import com.exemple.go4lunch.model.workmate.Workmate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkmateRepository {
    private final MutableLiveData<List<Workmate>> listOfWorkmate = new MutableLiveData<>();

    private static WorkmateRepository sWorkmateRepository;
    private static FirebaseHelper mFirebaseHelper;

    public static WorkmateRepository getInstance(){
        if(sWorkmateRepository == null){
            sWorkmateRepository = new WorkmateRepository();
        }
        return sWorkmateRepository;
    }

    public WorkmateRepository(){
        mFirebaseHelper = FirebaseHelper.getInstance();
        // Uncomment this method to populate your firebase database, it will upload some data
        // Comment it again after the first launch
        //initData();
    }

    public MutableLiveData<List<Workmate>> getAllWorkmate(){
        mFirebaseHelper.getAllWorkmate().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                ArrayList<Workmate> workmate = new ArrayList<>();
                for(QueryDocumentSnapshot document : task.getResult()){
                    workmate.add(document.toObject(Workmate.class));
                }
                listOfWorkmate.postValue(workmate);
            } else {
                Log.d("Error", "Error getting documents", task.getException());
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e){
                //handle error
                listOfWorkmate.postValue(null);
            }
        });
        return listOfWorkmate;
    }
/*
    public LiveData<Workmate> searchWorkmateByName(String name){
    }*/

    public void initData(){
        FirebaseHelper.getInstance().workmateRef.add(new Workmate("https://cdn-s-www.leprogres.fr/images/f918821f-62f0-4d0e-a004-2a67aa8ea4bc/NW_raw/raoul-duke-(johnny-depp)-dans-las-vegas-parano-de-terry-gilliam-photo-du-film-1530263043.jpg","Jonny Deep","1"));



    }
}
