package com.exemple.go4lunch;

import android.Manifest;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.exemple.go4lunch.ui.ViewModelFactory;
import com.exemple.go4lunch.ui.map.MapFragment;
import com.exemple.go4lunch.ui.map.MapViewModel;
import com.exemple.go4lunch.ui.restaurant.RestaurantListFragment;
import com.exemple.go4lunch.ui.restaurant.RestaurantListViewModel;
import com.exemple.go4lunch.ui.workmate.WorkmateFragment;
import com.exemple.go4lunch.ui.workmate.WorkmateViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

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

import com.exemple.go4lunch.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private ActivityMainBinding binding;
    private NavigationView drawerNavView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ViewModelFactory viewModelFactory;
    private MapViewModel mapFragmentViewModel;
    private RestaurantListViewModel restaurantListViewModel;
    private WorkmateViewModel workmateViewModel;
    private MapFragment mapFragment;
    private RestaurantListFragment restaurantListFragment;
    private WorkmateFragment workmateFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.checkUserLocationPermission();
        if(this.isGoogleServiceOK()) {
            this.initViewModel();
            this.createFragments();
            this.configureToolbar();
            this.configureBottomNavView();
            this.configureDrawerLayout();
            this.configureDrawerNavView();

            this.getResources().getString(R.string.MAPS_API_KEY);
            replaceFragment(mapFragment);
        }
    }

    private boolean isGoogleServiceOK() {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            //user can make map requests
            Log.d (TAG,"isServicesOK: Google Play services is working");
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOK: an error occured but can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initViewModel() {
        this.viewModelFactory = ViewModelFactory.getInstance();
        mapFragmentViewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);
        restaurantListViewModel = new ViewModelProvider(this, viewModelFactory).get(RestaurantListViewModel.class);
        workmateViewModel = new ViewModelProvider(this, viewModelFactory).get(WorkmateViewModel.class);
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
        this.restaurantListFragment = new RestaurantListFragment();
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
                    replaceFragment(restaurantListFragment);
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