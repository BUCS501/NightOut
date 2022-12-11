package com.example.nightout.api;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.nightout.BuildConfig;
import com.example.nightout.MapFragment;
import com.example.nightout.ui.restaurants.Restaurant;
import com.example.nightout.ui.restaurants.RestaurantsFragment;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class YelpRetrievalThread extends Thread {

    private static final String BASE_RADIUS = "4000";
    private static String BASE_URL = "https://api.yelp.com/v3/businesses/search?term=restaurants&latitude=current_lat&longitude=current_long&radius=current_radius&sort_by=best_match&limit=20";
    private final MapFragment originFragment;
    private String longitude;
    private String latitude;


    public YelpRetrievalThread(MapFragment mapFragment) {
        this.originFragment = mapFragment;
        getCoordinates();
    }

    public void run() {
        try {
            getRestaurants();
        } catch (IOException | ParseException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void getRestaurants() throws IOException, ParseException, JSONException {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        String url_str = BASE_URL.replace("current_lat", latitude).replace("current_long", longitude).replace("current_radius", BASE_RADIUS);
        URL url = new URL(url_str);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + BuildConfig.YELP_API_KEY);
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            System.out.println("Success");
        } else {
            System.out.println("Failed");
            return;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String result = in.readLine();
        JSONObject obj = (JSONObject) new JSONParser().parse(result);
        JSONArray arr = (JSONArray) obj.get("businesses");
        for (int i = 0; i < arr.size(); i++) {
            JSONObject restaurant = (JSONObject) arr.get(i);
            String id = (String) restaurant.get("id");
            String name = (String) restaurant.get("name");
            String address = (String) ((JSONObject) restaurant.get("location")).get("address1");
            String city = (String) ((JSONObject) restaurant.get("location")).get("city");
            String state = (String) ((JSONObject) restaurant.get("location")).get("state");
            String zip = (String) ((JSONObject) restaurant.get("location")).get("zip_code");
            String price = (String) restaurant.get("price");
            String imageUrl = (String) restaurant.get("image_url");
            double rating = (double) restaurant.get("rating");
            double longitude = (double) ((JSONObject) restaurant.get("coordinates")).get("longitude");
            double latitude = (double) ((JSONObject) restaurant.get("coordinates")).get("latitude");
            restaurants.add(new Restaurant(id, name, address, city, state, zip, price, imageUrl, rating, longitude, latitude));
        }
        originFragment.setRestaurantList(restaurants);
        in.close();
        Log.i("YelpRetrievalThread", "Reached end of API calls");

    }

    public void getCoordinates() {
        SharedPreferences sharedPreferences = originFragment.getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
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
    }
}