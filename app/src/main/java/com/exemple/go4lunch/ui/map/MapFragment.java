package com.exemple.go4lunch.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.exemple.go4lunch.R;
import com.exemple.go4lunch.data.permission_checker.PermissionUtils;
import com.exemple.go4lunch.data.restaurant.nerbysearch.Result;
import com.exemple.go4lunch.databinding.FragmentMapBinding;
import com.exemple.go4lunch.ui.ViewModelFactory;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback  {

    private FragmentMapBinding binding;
    private MapViewModel mapViewModel;




    private static final String TAG = MapFragment.class.getSimpleName();
    GoogleMap mapsView;

    SupportMapFragment supportMapFragment;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng defaultLocation = new LatLng(48.888053, 2.343312);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 999;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean permissionDenied = false;
    int REQ_PERMISSION = 100;


    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    //Keys for storing activity state
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMapBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        initMap();

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLocationPermission();



    }

    private void getLocationPermission() {
        //request location permission
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    private void initMap() {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_view);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
        updateLocationUI();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mapsView = googleMap;
        mapsView.setOnMyLocationButtonClickListener(this);
        mapsView.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mapsView != null) {
                mapsView.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }


    /**
     * move camera on user
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    public MapFragment() {

    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //maps data



    private void getMapViewModel() {
        mapViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapViewModel.class);
    }

    private void setUpMarker() {
        mapViewModel.getRestaurantMapViewStateLiveData().observe(getViewLifecycleOwner(), restaurantMapViewState -> {
            displayMarker(restaurantMapViewState.getRestaurantMapResult());
        });
    }

    private void displayMarker(List<Result> results) {

        for (Result result : results) {
            LatLng latLngMarker = new LatLng(
                    (result.getGeometry()
                            .getLocation()
                            .getLat()),
                    (result.getGeometry()
                            .getLocation()
                            .getLng()));
            //get LatLong to Marker
            mapsView.addMarker(new MarkerOptions()
                    .position(latLngMarker)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .title(result.getName()));
            //show marker
            LatLng latLngResult = new LatLng(
                    (result.getGeometry()
                            .getLocation()
                            .getLat()),
                    (result.getGeometry()
                            .getLocation()
                            .getLng()));
            //set position marker
            mapsView.moveCamera(CameraUpdateFactory.newLatLng(latLngResult));
            mapsView.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngResult, 14));
            mapsView.getUiSettings().setAllGesturesEnabled(true);
            mapsView.getUiSettings().setZoomGesturesEnabled(true);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        this.permissionDenied = true;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                permissionDenied = false;
                enableMyLocation();
            } else {
                Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mapsView != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mapsView.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }



    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (mapsView == null) {
            return;
        }
        try {
            if (!permissionDenied) {
                mapsView.setMyLocationEnabled(true);
                mapsView.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mapsView.setMyLocationEnabled(false);
                mapsView.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    // [END maps_current_place_update_location_ui]
}