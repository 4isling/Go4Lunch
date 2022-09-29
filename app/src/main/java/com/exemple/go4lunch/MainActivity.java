package com.exemple.go4lunch;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.exemple.go4lunch.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        this.configureBottomNavView();
        this.configureDrawerLayout();
    }

    private void configureDrawerLayout() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        binding.drawerLayout.addDrawerListener(toggle);
    }

    private void configureToolbar(){
        setSupportActionBar(binding.toolbar);
    }

    private void configureBottomNavView(){
        this.navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_restaurant, R.id.navigation_workmate)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public void onBackPressed(){
        if(this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.binding.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            finish();
        }
    }

}