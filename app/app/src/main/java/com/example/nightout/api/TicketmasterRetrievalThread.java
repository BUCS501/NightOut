package com.example.nightout.api;

import com.example.nightout.ui.events.Event;
import com.example.nightout.ui.events.EventFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class TicketmasterRetrievalThread extends Thread{
    private static final String TEST_ZIP = "02215";
    private static final String TEST_URL = "https://app.ticketmaster.com/discovery/v2/events.json?postalCode=" + TEST_ZIP + "&apikey=" + "TicketmasterAPIKey.API_KEY";
    private EventFragment originFragment;

    public TicketmasterRetrievalThread(EventFragment eventsFragment) {
        this.originFragment = eventsFragment;
    }

    public void run() {
        try {
            getEvents();
        } catch (IOException | JSONException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    public void getEvents() throws IOException, JSONException, org.json.simple.parser.ParseException {
        ArrayList<Event> eventsList = new ArrayList<Event>();
        URL url = new URL(TEST_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
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
        JSONParser parse = new JSONParser();
        JSONObject data_obj = (JSONObject) parse.parse(result);
        JSONObject embedded = (JSONObject) data_obj.get("_embedded");
        JSONArray events = (JSONArray) embedded.get("events");
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = (JSONObject) events.get(i);
            String name = (String) event.get("name");
            String description = (String) event.get("description");
            JSONObject dates = (JSONObject) event.get("dates");
            JSONObject start = (JSONObject) dates.get("start");
            String date = (String) start.get("localDate");
            String time = (String) start.get("localTime");
            JSONObject _embedded = (JSONObject) event.get("_embedded");
            JSONArray venues = (JSONArray) _embedded.get("venues");
            JSONObject venue = (JSONObject) venues.get(0);
            String location = (String) venue.get("name");
            JSONArray priceRanges = (JSONArray) event.get("priceRanges");
            JSONObject priceRange = (JSONObject) priceRanges.get(0);
            String price = (String) priceRange.get("min");
            JSONArray images = (JSONArray) event.get("images");
            JSONObject image = (JSONObject) images.get(0);
            String imageUrl = (String) image.get("url");
            Event eventCurr = new Event(name, description, date, time, location, price, imageUrl);
            eventsList.add(eventCurr);




    }
        originFragment.setEvents(eventsList);
    }
}
