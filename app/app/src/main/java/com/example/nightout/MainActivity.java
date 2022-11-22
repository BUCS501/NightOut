package com.example.nightout;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nightout.ui.events.EventFragment;
import com.example.nightout.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nightout.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

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

                switch (item.getItemId()) {
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