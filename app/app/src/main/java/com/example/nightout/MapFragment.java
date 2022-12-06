package com.example.nightout;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.nightout.ui.events.Event;
import com.example.nightout.ui.restaurants.Restaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap mMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    Context mContext;
    List<Restaurant> restaurantList;
    List<Event> eventList;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        getCurrentLocation();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String restaurantListString = sharedPreferences.getString("current_restaurants", null);
        String eventListString = sharedPreferences.getString("current_events", null);
        if (restaurantListString != null) {
            Type type = new TypeToken<List<Restaurant>>(){}.getType();
            // Usable List of restaurants to parse for LatLong info
            restaurantList = new Gson().fromJson(restaurantListString, type);
        }

        if (eventListString != null) {
            Type type = new TypeToken<List<Event>>(){}.getType();
            // Usable List of events to parse for LatLong info
            eventList = new Gson().fromJson(eventListString, type);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;



        LatLng currLoc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currLoc).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // When clicked on map
                // Initialize marker options
                MarkerOptions markerOptions = new MarkerOptions();
                // Set position of marker
                markerOptions.position(latLng);
                // Set title of marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                // Remove all marker
                googleMap.clear();
                // Animating to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                // Add marker on map
                googleMap.addMarker(markerOptions);
            }
        });

        if (restaurantList != null) {
            for (Restaurant restaurant : restaurantList) {
                LatLng restaurant_LatLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                mMap.addMarker(new MarkerOptions().position(restaurant_LatLng).title(restaurant.getName()));
            }
        }

        if (eventList != null) {
            for (Event event : eventList) {
                LatLng event_LatLng = new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(event_LatLng).title(event.getName()));
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    currentLocation = location;
//                    Toast.makeText(mContext, (int) currentLocation.getLatitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapFragment.this);

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEditor = sharedPreferences.edit();
                    myEditor.putString("device_latitude", String.valueOf(currentLocation.getLatitude()));
                    myEditor.putString("device_longitude", String.valueOf(currentLocation.getLongitude()));
                    myEditor.commit();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (REQUEST_CODE){
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
                break;
        }
    }
}