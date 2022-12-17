package com.exemple.go4lunch.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.exemple.go4lunch.R;
import com.exemple.go4lunch.databinding.FragmentMapBinding;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private SupportMapFragment supportMapFragment;

    private static final String TAG = MapFragment.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition cameraPosition;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(48.888053, 2.343312);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 999;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private LatLng userLocation;
    private String locationString;

    //Keys for storing activity state
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMapBinding.inflate(getLayoutInflater());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLocationPermission();

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.initFAB();
        View root = binding.getRoot();
        initMap();

        return root;
    }

    private void initFAB() {
        binding.fabLocation.setOnClickListener(view -> getUserLocation());
    }


    public MapFragment(){

    }

    public static MapFragment newInstance(){
        return new MapFragment();
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
            .findFragmentById(R.id.map_view);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
/*
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(37.4219999,-122.0862462))
                        .zoom(10)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

            }
        });
  */  }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //maps data

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (userLocation != null ){
            googleMap.addMarker(new MarkerOptions()
                    .position(userLocation)
                    .title("location"));
        }

        if(userLocation == null && lastKnownLocation != null){
            LatLng lastKnowLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(lastKnowLatLng)
                    .title("last know location")
            );
        }
        if(userLocation == null && lastKnownLocation == null){
            googleMap.addMarker(new MarkerOptions()
                    .position(defaultLocation)//18eme Paris
                    .title("default location"));
        }
    }

    private void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                String locationString = lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                updateWithPosition();
                //getNearbyRestaurant();
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        0
                );
            } else {
                if (!isLocationEnable()) {
                    openLocationDialog();
                } else {
                    getCurrentLocation();
                }
            }
        });
    }

    private void getNearbyRestaurant() {
       // restaurantRepository.getAllRestaurants
    }

    private Boolean isLocationEnable() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void openLocationDialog() {
        new AlertDialog.Builder(requireContext())
                .setMessage("enable location")
                .setPositiveButton("open location settings", (dialogInterface, i) -> requireContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setOnDismissListener(DialogInterface::cancel).show();
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        CurrentLocationRequest currentLocationRequest = new CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).setDurationMillis(3000).build();
        fusedLocationProviderClient.getCurrentLocation(currentLocationRequest, null).addOnSuccessListener(location -> {
            locationString = location.getLatitude() + "," + location.getLongitude();
            Log.e("locationRequestTest", locationString);
            updateWithPosition();
            //getNearbyRestaurant();
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        if(mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION,mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("MissingPermission")
    protected void updateWithPosition(){
        if(mMap != null){
            if(ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            }else{
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }
}