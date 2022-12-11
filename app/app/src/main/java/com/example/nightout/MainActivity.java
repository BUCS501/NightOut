package com.example.nightout;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.nightout.ui.account.AcccountFragment;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

import com.example.nightout.ui.events.EventFragment;
import com.example.nightout.ui.restaurants.RestaurantsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetLists();
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                EventFragment eventFragment = new EventFragment();
                RestaurantsFragment restaurantsFragment = new RestaurantsFragment();
                Fragment mapFragment = new MapFragment();
                AcccountFragment acccountFragment = new AcccountFragment();
                NoNetworkFragment noNetworkFragment = new NoNetworkFragment();
                if (!isNetworkConnected()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, noNetworkFragment).commit();
                    return false;
                }
                switch (item.getItemId()) {
                    case R.id.navigation_maps:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, mapFragment).commit();
                        return true;
                    case R.id.navigation_restaurants:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, restaurantsFragment).commit();
                        return true;
                    case R.id.navigation_events:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, eventFragment).commit();
                        return true;
                    case R.id.navigation_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, acccountFragment).commit();
                        return true;
                }
                return false;

            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void resetLists() {
        ArrayList<Restaurant> restaurantList = new ArrayList<>();
        ArrayList<Event> eventList = new ArrayList<>();
        SharedPreferences.Editor myEdit = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
        String restaurantListString = new Gson().toJson(restaurantList);
        myEdit.putString("current_restaurants", restaurantListString);
        String eventListString = new Gson().toJson(eventList);
        myEdit.putString("current_events", eventListString);
        myEdit.apply();
    }

}