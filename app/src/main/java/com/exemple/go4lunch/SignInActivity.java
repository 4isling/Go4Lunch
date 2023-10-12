package com.exemple.go4lunch;

import static com.exemple.go4lunch.BuildConfig.APPLICATION_ID;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.exemple.go4lunch.databinding.ActivitySigninBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 666;
    private static final String TAG = "SignInActivity";
    private Application application;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig> providers;

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.exemple.go4lunch.databinding.ActivitySigninBinding binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        application = getApplication();
        FirebaseApp.initializeApp(this);
        boolean isMain = isMainProcess(this);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(isMain).build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        if (!isMain) {
            // other things
            return;
        }
        // other things
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
        initFirebaseAuth();
    }

    private boolean isMainProcess(Context context) {
        if (null == context) {
            return true;
        }
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (APPLICATION_ID.equals(processInfo.processName) && pid == processInfo.pid) {
                return true;
            }
        }
        return false;
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onStop() {
        if(listener != null)firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!this.isCurrentUserLogged()){
            this.initFirebaseAuth();
        }
    }



    public void initFirebaseAuth(){
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        listener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null)
            {
                Toast.makeText(SignInActivity.this, "You already login with uid"+ user.getUid(), Toast.LENGTH_SHORT).show();
            }
            else
            {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setIsSmartLockEnabled(false, true)
                                .build(),
                        RC_SIGN_IN);
            }
        };
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if(result.getResultCode() == RESULT_OK){
            //on User SignIn = true;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Log.d(TAG, "onActivityResult:" + user.getEmail());

            if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){
                Toast.makeText(this,"@string/welcome_new_user", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"@string/welcome_back", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            if(response==null){
                Log.d(TAG, "onActivityResult:User cancel sign in request");
            }else{
                Log.d(TAG, "onActivityResult:", response.getError());
            }
        }
    }


    protected FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                //on User signin = true;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Log.d(TAG, "onActivityResult:" + user.getEmail());

                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){
                    Toast.makeText(this,"@string/welcome_new_user", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"@string/welcome_back", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }else{
                if(response==null){
                    Log.d(TAG, "onActivityResult:User cancel sign in request");
                }else{
                    Log.d(TAG, "onActivityResult:", response.getError());
                }
            }
        }
    }

}
