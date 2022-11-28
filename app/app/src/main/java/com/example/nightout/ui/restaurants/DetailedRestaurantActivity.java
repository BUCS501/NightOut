package com.example.nightout.ui.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.nightout.R;

public class DetailedRestaurantActivity extends AppCompatActivity {

    private ImageView restaurantImage;
    private TextView restaurantName;
    private TextView restaurantAddress;
    private TextView restaurantPhoneNumber;
    private TextView restaurantWebsite;
    private TextView restaurantPrice;
    private RatingBar rbRestaurant; // TODO: change to proper naming convention
    private Button callButton;
    private Button directionsButton;
    private TableLayout restaurantHours;
    private DetailedRestaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_restaurant);

        restaurantImage = (ImageView) findViewById(R.id.restaurantImage);
        restaurantName = (TextView) findViewById(R.id.restaurantName);
        restaurantAddress = (TextView) findViewById(R.id.restaurantAddress);
        restaurantPhoneNumber = (TextView) findViewById(R.id.restaurantPhoneNumber);
        restaurantWebsite = (TextView) findViewById(R.id.restaurantWebsite);
        restaurantPrice = (TextView) findViewById(R.id.restaurantPrice);
        rbRestaurant = (RatingBar) findViewById(R.id.rbRestaurant);
        callButton = (Button) findViewById(R.id.callBtn);
        directionsButton = (Button) findViewById(R.id.directionsBtn);
    restaurantHours = (TableLayout) findViewById(R.id.restaurantHours);


    }
}