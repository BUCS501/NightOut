package com.example.nightout;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nightout.ui.account.AcccountFragment;
import com.example.nightout.ui.events.EventFragment;
import com.example.nightout.ui.restaurants.RestaurantsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CentralActivity extends AppCompatActivity {

    private static final String TEST_ZIP = "02215";
    private static final String TEST_URL = "https://api.yelp.com/v3/businesses/search?term=restaurants&location=" + TEST_ZIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                EventFragment eventFragment = new EventFragment();
                AcccountFragment acccountFragment = new AcccountFragment();
                RestaurantsFragment restaurantsFragment = new RestaurantsFragment();

                switch (item.getItemId()) {
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

}

