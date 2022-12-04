package com.example.nightout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nightout.databinding.ActivityMainBinding;
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
import com.example.nightout.ui.home.HomeFragment;
import com.example.nightout.ui.restaurants.RestaurantsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_ZIP = "02215";
    private static final String TEST_URL = "https://api.yelp.com/v3/businesses/search?term=restaurants&location=" + TEST_ZIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                EventFragment eventFragment = new EventFragment();
                HomeFragment homeFragment = new HomeFragment();
                RestaurantsFragment restaurantsFragment = new RestaurantsFragment();
                Fragment mapFragment = new MapFragment();

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
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                        return true;
                }
                return false;

            }
        });





    }

}