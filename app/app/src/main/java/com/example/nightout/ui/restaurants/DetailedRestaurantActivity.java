package com.example.nightout.ui.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nightout.R;
import com.example.nightout.SignUp;
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
    private Button websiteButton;
    private Button bookmarkButton;
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

        // Create onClickListeners for the buttons
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurant.getPhoneNumber()));
                startActivity(intent);
            }
        });

        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getMapsUrl()));
                startActivity(intent);
            }
        });

        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getRestaurantUrl()));
                startActivity(intent);
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Insert bookmark functionality
                Toast.makeText(getApplicationContext(), ("User: " + SignUp.user + ", saved: " + restaurant.getName()), Toast.LENGTH_SHORT).show();
            }
        });

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
        websiteButton = (Button) findViewById(R.id.websiteBtn);
        bookmarkButton = (Button) findViewById(R.id.bookmarkBtn);
        restaurantHours = (TableLayout) findViewById(R.id.restaurantHours);
    }

    public void setViews() {
        restaurantName.setText(restaurant.getName());
        restaurantAddress.setText(restaurant.getFormattedAddress());
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

    public String getMapsUrl() {
        char[] address = restaurant.getFormattedAddress().toCharArray();
        StringBuilder mapsUrl = new StringBuilder("https://www.google.com/maps/search/?api=1&query=");
        for (char c : address) {
            if (c == ' ') {
                mapsUrl.append("+");
            } else if (c == ',') {
                mapsUrl.append("%2C");
            } else {
                mapsUrl.append(c);
            }
        }
        return mapsUrl.toString();
    }

    public DetailedRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(DetailedRestaurant restaurant) {
        this.restaurant = restaurant;
    }

}