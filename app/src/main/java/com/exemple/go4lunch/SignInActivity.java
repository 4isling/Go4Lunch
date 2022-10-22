package com.exemple.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    protected GoogleSignInOptions gso;
    protected GoogleSignInClient gsc;


    private static final int RC_SIGN_IN = 666;
    private static final String TAG = "SignInActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener listener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_signin);
        firebaseAuth = FirebaseAuth.getInstance();
        //check if user already logged
        if(firebaseAuth.getCurrentUser() != null){

            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }

        initFirebaseAuth();
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!this.isCurrentUserLogged()){
            this.initFirebaseAuth();
        }
    }

    @Override
    protected void onStop() {
        if(listener != null)firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    private void initFirebaseAuth(){
        List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
        );
        this.signInFirebase(provider);
    }

    private void signInFirebase(List<AuthUI.IdpConfig> provider){
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(provider)
                        .setIsSmartLockEnabled(false,true)
                        .setLogo(R.drawable.ic_go4lunch_logo)
                        .build(),
                RC_SIGN_IN);
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
            if(resultCode == RESULT_OK){
                //on User signin = true;
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
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response==null){
                    Log.d(TAG, "onActivityResult:User cancel sign in request");
                }else{
                    Log.d(TAG, "onActivityResult:", response.getError());
                }
            }
        }
    }
    ///function useless
    private void startSignInActivity(){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers =
                Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_go4lunch_logo)
                        .build(),
                RC_SIGN_IN);
    }

    ///function useless
    private void navigateToMainActivity(){
        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
