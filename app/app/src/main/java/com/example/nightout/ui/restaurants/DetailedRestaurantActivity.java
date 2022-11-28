package com.example.nightout.ui.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.nightout.R;
import com.example.nightout.api.DetailedYelpRetrievalThread;
import com.example.nightout.api.ImageRetrievalThread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailedRestaurantActivity extends AppCompatActivity {

    private ImageView restaurantImage;
    private TextView restaurantName;
    private TextView restaurantAddress;
    private TextView restaurantPhoneNumber;
    private TextView restaurantPrice;
    private RatingBar rbRestaurant; // TODO: change to proper naming convention
    private Button callButton;
    private Button directionsButton;
    private TableLayout restaurantHours;
    private String restaurantID;
    private DetailedRestaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_restaurant);

        // Gets all of the views from the layout and assigns them to variables
        initializeViews();

        // Gets the restaurant ID from the intent of the RestaurantList Fragment
        Intent intent = getIntent();
        restaurantID = intent.getExtras().getString("restaurantID");

        // Creates a new thread to get the restaurant information from the Yelp API
        callDetailedYelpAPI();
        assert restaurant != null;
        // Calls method to set the views to the restaurant information
        setViews();

    }

    public void initializeViews() {
        restaurantImage = (ImageView) findViewById(R.id.restaurantImage);
        restaurantName = (TextView) findViewById(R.id.restaurantName);
        restaurantAddress = (TextView) findViewById(R.id.restaurantAddress);
        restaurantPhoneNumber = (TextView) findViewById(R.id.restaurantPhoneNumber);
        restaurantPrice = (TextView) findViewById(R.id.restaurantPrice);
        rbRestaurant = (RatingBar) findViewById(R.id.rbRestaurant);
        rbRestaurant.setIsIndicator(true);
        callButton = (Button) findViewById(R.id.callBtn);
        directionsButton = (Button) findViewById(R.id.directionsBtn);
        restaurantHours = (TableLayout) findViewById(R.id.restaurantHours);
    }

    public void setViews() {
        restaurantName.setText(restaurant.getName());
        restaurantAddress.setText(restaurant.getAddress());
        restaurantPhoneNumber.setText(restaurant.getDisplayedPhoneNumber());
        restaurantPrice.setText(restaurant.getPrice());
        rbRestaurant.setRating((float) restaurant.getRating());
        setRestaurantHoursTable();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new ImageRetrievalThread(restaurant.getImageUrl(), restaurantImage));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        executor.shutdownNow();
    }

    public void setRestaurantHoursTable() {
        for(int i = 1; i < restaurantHours.getChildCount(); i++) {
            TableRow row = (TableRow) restaurantHours.getChildAt(i);
            TextView daysHours = (TextView) row.getChildAt(1);
            daysHours.setText(restaurant.getFormattedHoursForDay(i-1));
        }
    }

    public void callDetailedYelpAPI() {
        // create an executor service to run the thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Calls the Detailed Yelp API and sets the detailed to the results
        executor.execute(new DetailedYelpRetrievalThread(restaurantID, this));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        // terminate executor
        executor.shutdownNow();
        System.out.println();
    }

    public DetailedRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(DetailedRestaurant restaurant) {
        this.restaurant = restaurant;
    }

}