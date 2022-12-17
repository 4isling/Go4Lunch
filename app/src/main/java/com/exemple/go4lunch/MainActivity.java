package com.exemple.go4lunch;

import android.Manifest;
import android.app.Application;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.exemple.go4lunch.data.permission_checker.PermissionChecker;
import com.exemple.go4lunch.ui.ViewModelFactory;
import com.exemple.go4lunch.ui.map.MapFragment;
import com.exemple.go4lunch.ui.map.MapViewModel;
import com.exemple.go4lunch.ui.restaurant.RestaurantFragment;
import com.exemple.go4lunch.ui.restaurant.RestaurantViewModel;
import com.exemple.go4lunch.ui.workmate.WorkmateFragment;
import com.exemple.go4lunch.ui.workmate.WorkmateViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.exemple.go4lunch.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.core.View;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private NavigationView drawerNavView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private MapViewModel mapFragmentViewModel;
    private RestaurantViewModel restaurantViewModel;
    private WorkmateViewModel workmateViewModel;
    private MapFragment mapFragment;
    private RestaurantFragment restaurantFragment;
    private WorkmateFragment workmateFragment;

//@TODO retirer de main activity l'override de activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initViewModel();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.createFragments();
        this.configureToolbar();
        this.configureBottomNavView();
        this.configureDrawerLayout();
        this.configureDrawerNavView();
        this.checkUserLocationPermission();

        this.getResources().getString(R.string.MAPS_API_KEY);
        replaceFragment(mapFragment);
    }

    private void initViewModel() {
        mapFragmentViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapViewModel.class);
        restaurantViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantViewModel.class);
        workmateViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(WorkmateViewModel.class);
    }

    private void checkUserLocationPermission(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                0
        );

    }

    private void createFragments(){
        this.mapFragment = new MapFragment();
        this.restaurantFragment = new RestaurantFragment();
        this.workmateFragment = new WorkmateFragment();
    }

    private void configureToolbar(){
        this.toolbar = binding.activityMainToolbar;
        binding.activityMainToolbar.setNavigationIcon(R.drawable.ic_baseline_list_24);
        setSupportActionBar(binding.activityMainToolbar);
    }

    private void configureDrawerLayout() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void configureDrawerNavView(){
        this.drawerNavView = binding.mainDrawerNavView;
        drawerNavView.setNavigationItemSelectedListener(this);
    }

    private void configureBottomNavView(){
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_map:
                    replaceFragment(mapFragment);
                    break;

                case R.id.navigation_restaurant:
                    replaceFragment(restaurantFragment);
                    break;

                case R.id.navigation_workmate:
                    replaceFragment(workmateFragment);
                    break;
            }return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed(){
        if(this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.binding.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.your_lunch:
                break;

            case R.id.settings:
                break;

            case R.id.logout:
                break;

            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}