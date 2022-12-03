package com.example.nightout.api;

import android.util.Log;

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

    private static final String TEST_ZIP = "02215";
    private static final String TEST_URL = "https://api.yelp.com/v3/businesses/search?term=restaurants&location=" + TEST_ZIP;
    private final RestaurantsFragment originFragment;


    public YelpRetrievalThread(RestaurantsFragment restaurantsFragment) {
        this.originFragment = restaurantsFragment;
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
        URL url = new URL(TEST_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + "YelpAPIKey.API_KEY");
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
            restaurants.add(new Restaurant(id, name, address, city, state, zip, price, imageUrl, rating));
        }
        originFragment.setRestaurants(restaurants);
        in.close();
        Log.i("YelpRetrievalThread", "Reached end of API calls");

    }
}