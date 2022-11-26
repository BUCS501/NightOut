package com.example.nightout.api;

import com.example.nightout.ui.restaurants.DetailedRestaurant;
import com.example.nightout.ui.restaurants.Hours;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.CollationKey;
import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

public class DetailedYelpRetrievalThread extends Thread {

    private static final String BASE_URL = "https://api.yelp.com/v3/businesses/";
    private final String id;
    // TODO: decide if we want to use a fragment or an activity to display the detailed restaurant

    public DetailedYelpRetrievalThread(String id) {
        this.id = id;
    }

    public DetailedYelpRetrievalThread() {
        this.id = "WavvLdfdP6g8aZTtbBQHTw";
    }

    public void run() {
        try {
            getDetailedRestaurant();
        } catch (IOException | JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void getDetailedRestaurant() throws IOException, JSONException, ParseException {
        URL url = new URL(BASE_URL + id);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + YelpAPIKey.API_KEY);
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
        JSONObject restaurant = (JSONObject) new JSONParser().parse(result);
        String id = (String) restaurant.get("id");
        String name = (String) restaurant.get("name");
        String address = (String) ((JSONObject) restaurant.get("location")).get("address1");
        String city = (String) ((JSONObject) restaurant.get("location")).get("city");
        String state = (String) ((JSONObject) restaurant.get("location")).get("state");
        String zip = (String) ((JSONObject) restaurant.get("location")).get("zip_code");
        String price = (String) restaurant.get("price");
        String imageUrl = (String) restaurant.get("image_url");
        double rating = (double) restaurant.get("rating");
        String phone = (String) restaurant.get("phone");
        String displayedPhone = (String) restaurant.get("display_phone");
        String urlStr = (String) restaurant.get("url");
        JSONArray hoursOuter = (JSONArray) restaurant.get("hours");
        JSONArray hoursInner = (JSONArray) ((JSONObject) hoursOuter.get(0)).get("open");
        ArrayList<Hours> hours = parseHours(hoursInner);
        Collections.sort(hours);
        DetailedRestaurant detailedRestaurant = new DetailedRestaurant(id, name, address, city, state, zip, price, imageUrl, rating, phone, displayedPhone, urlStr, hours);
    }

    private ArrayList<Hours> parseHours(JSONArray hoursInner) {
        ArrayList<Hours> hours = new ArrayList<>();
        for (int i = 0; i < hoursInner.size(); i++) {
            JSONObject day = (JSONObject) hoursInner.get(i);
            long dayOfWeek = (long) day.get("day");
            String start = (String) day.get("start");
            String end = (String) day.get("end");
            hours.add(new Hours(dayOfWeek, start, end));
        }
        return hours;
    }
}
