package com.exemple.go4lunch.data;
import com.exemple.go4lunch.model.workmate.Workmate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseHelper {
    private static FirebaseHelper sFirebaseHelper;


    public static FirebaseHelper getInstance(){
        if(sFirebaseHelper == null){
            sFirebaseHelper = new FirebaseHelper();
        }
        return sFirebaseHelper;

    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final CollectionReference workmateRef = db.collection("workmates");

    public Task<QuerySnapshot> getAllWorkmate(){
        return workmateRef.get();
    }


/*
    public static Task<Void> createUser(Workmate workmate){
        return FirebaseFirestore
    }*/

}
