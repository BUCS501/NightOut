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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nightout.api.TicketmasterRetrievalThread;
import com.example.nightout.api.YelpRetrievalThread;
import com.example.nightout.ui.events.Event;
import com.example.nightout.ui.restaurants.Restaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Context mContext;
    ArrayList<Restaurant> restaurantList;
    ArrayList<Event> eventList;
//    LatLng restoringLoc;
    private String current_latitude;
    private String current_longitude;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        getCurrentLocation();

//        if (savedInstanceState != null ){
//            restoringLoc = new LatLng(savedInstanceState.getDouble("stored_lat"), savedInstanceState.getDouble("stored_long"));
//        }
        getListsFromSharedPreferences();
        
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
        LatLng currPin = getCoordinates();
        mMap.addMarker(new MarkerOptions().position(currPin).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currPin));

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

                // restoring the pin location when back from other fragment
//                if (restoringLoc != null){
//                    mMap.clear();
//                    mMap.addMarker(new MarkerOptions().position(restoringLoc));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(restoringLoc, 15));
//                }

                // Get latitude and longitude of clicked location
                current_latitude = String.valueOf(latLng.latitude);
                current_longitude = String.valueOf(latLng.longitude);

                addPinLocationtoSharedPref();
                // Call APIs necessary to get data
                callYelpRetrievalThread();
                callTMRetrievalThread();

                addPins();
            }
        });
        if (MainActivity.firstTimeMap) {
            addPinLocationtoSharedPref();
            callYelpRetrievalThread();
            callTMRetrievalThread();
            MainActivity.firstTimeMap = false;
        }
        addPins();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            NoLocationPermsFragment noLocationPermsFragment = new NoLocationPermsFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, noLocationPermsFragment).commit();
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
                    addDeviceLocationtoSharedPref(currentLocation);
                }
            }
        });
    }

    public void addRestaurantPins() {
        if (restaurantList != null) {
            for (Restaurant restaurant : restaurantList) {
                LatLng restaurant_LatLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).position(restaurant_LatLng).title(restaurant.getName()));
            }
        }
    }

    public void addEventPins() {
        if (eventList != null) {
            for (Event event : eventList) {
                LatLng event_LatLng = new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()));
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).position(event_LatLng).title(event.getName()));
            }
        }
    }

    public void addPins() {
        addRestaurantPins();
        addEventPins();
    }

    public void addDeviceLocationtoSharedPref(Location currentLocation) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferences.edit();
        myEditor.putString("device_latitude", String.valueOf(currentLocation.getLatitude()));
        myEditor.putString("device_longitude", String.valueOf(currentLocation.getLongitude()));
        myEditor.commit();
    }

    public void addPinLocationtoSharedPref() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferences.edit();
        myEditor.putString("pin_latitude", current_latitude);
        myEditor.putString("pin_longitude", current_longitude);
        myEditor.commit();
    }

    public void callYelpRetrievalThread() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Calls the Yelp API and sets the restaurants array to the results
        executor.execute(new YelpRetrievalThread(this));
        //executor.execute(new DetailedYelpRetrievalThread());
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        // terminate executor
        executor.shutdownNow();
        System.out.println();
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        // Storing the arraylist of restaurants into the shared preferences
        String restaurantListString = new Gson().toJson(restaurantList);
        myEdit.putString("current_restaurants", restaurantListString);
        myEdit.commit();
    }

    public void callTMRetrievalThread() {
        // create an executor service to run the thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Calls the TicketMaster API and sets the events array to the results
        executor.execute(new TicketmasterRetrievalThread(this));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        // terminate executor
        executor.shutdownNow();
        System.out.println();

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        String eventsListString = new Gson().toJson(eventList);
        myEdit.putString("current_events", eventsListString);
        myEdit.commit();
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

    public String getCurrent_latitude() {
        return current_latitude;
    }

    public void setCurrent_latitude(String current_latitude) {
        this.current_latitude = current_latitude;
    }

    public String getCurrent_longitude() {
        return current_longitude;
    }

    public void setCurrent_longitude(String current_longitude) {
        this.current_longitude = current_longitude;
    }

    public void setRestaurantList(ArrayList<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public ArrayList<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setEventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    public void getListsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String restaurantListString = sharedPreferences.getString("current_restaurants", null);
        String eventListString = sharedPreferences.getString("current_events", null);
        if (restaurantListString != null) {
            Type type = new TypeToken<List<Restaurant>>(){}.getType();
            // Usable List of restaurants to parse for LatLong info
            restaurantList = new Gson().fromJson(restaurantListString, type);
        }
        if (eventListString != null) {
            Type type = new TypeToken<List<Event>>() {
            }.getType();
            // Usable List of events to parse for LatLong info
            eventList = new Gson().fromJson(eventListString, type);
        }
    }

    public LatLng getCoordinates() {
        SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String latitude;
        String longitude;
        // get pin location if it has been set
        latitude = sharedPreferences.getString("pin_latitude", null);
        longitude = sharedPreferences.getString("pin_longitude", null);
        if (latitude == null && longitude == null) {
            // get device current location if pin hasn't been set
            latitude = sharedPreferences.getString("device_latitude", null);
            longitude = sharedPreferences.getString("device_longitude", null);
        }
        if (latitude == null && longitude == null) {
            // if device location is not available, set to default location
            latitude = "42.3601";
            longitude = "-71.0589";
        }
        return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }
}