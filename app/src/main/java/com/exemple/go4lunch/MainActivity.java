package com.exemple.go4lunch;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.exemple.go4lunch.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration mToolBarConfiguration;
    private AppBarConfiguration mBottomNavViewBarConfiguration;
    private BottomNavigationView bottomNavView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.configureToolbar();
        this.configureBottomNavView();
        this.configureDrawerLayout();

    }

    private void configureToolbar(){
        binding.activityMainToolbar.setNavigationIcon(R.drawable.ic_baseline_list_24);
        setSupportActionBar(binding.activityMainToolbar);
    }

    private void configureDrawerLayout() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.mainDrawerNavView;
        mToolBarConfiguration = new AppBarConfiguration.Builder(
                R.id.your_lunch, R.id.settings, R.id.logout)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.activity_main_frame_layout);
        NavigationUI.setupActionBarWithNavController(this, navController,mBottomNavViewBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void configureBottomNavView(){
        this.bottomNavView = findViewById(R.id.bottom_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mBottomNavViewBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_restaurant, R.id.navigation_workmate)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.activity_main_frame_layout);
        NavigationUI.setupActionBarWithNavController(this, navController, mBottomNavViewBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavView, navController);
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