package com.example.nightout.api;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.example.nightout.BuildConfig;
import com.example.nightout.MapFragment;
import com.example.nightout.ui.events.Event;
import com.example.nightout.ui.events.EventFragment;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class TicketmasterRetrievalThread extends Thread {
    private static final String API_KEY = BuildConfig.TM_API_KEY;
    private static final String API_URL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + API_KEY;

    private final MapFragment originFragment;
    private final EventFragment eventFragment;
    private String latitude;
    private String longitude;
    private String classificationName;
    private final String radius = "10";


    public TicketmasterRetrievalThread(MapFragment mapFragment) {
        this.originFragment = mapFragment;
        this.eventFragment = null;
        this.classificationName = "";
        getCoordinates();
    }

    public TicketmasterRetrievalThread(EventFragment eventFragment, String classificationName) {
        this.eventFragment = eventFragment;
        this.originFragment = null;
        this.classificationName = classificationName;
        getCoordinates();
    }

    public void run() {
        try {
            getEvents(classificationName, 20, latitude + "," + longitude);
        } catch (IOException | JSONException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    public void getEvents(String classificationName, int n, String latlon) throws IOException, JSONException, org.json.simple.parser.ParseException {
        ArrayList<Event> eventsList = new ArrayList<Event>();
        String filterDate = getFutureDate(30);
        URL url;
        if (classificationName.equals("")) {
            url = new URL(API_URL + "&latlong=" + latlon + "&radius=" + radius + "&size=" + n + "&endDateTime=" + filterDate);
        } else {
            url = new URL(API_URL + "&latlong=" + latlon  + "&radius=" + radius +"&size=" + n + "&endDateTime=" + filterDate + "&classificationName=" + classificationName);
        }
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
        if (embedded == null) {
            return;
        }
        JSONArray events = (JSONArray) embedded.get("events");
        for (int i = 0; i < events.size(); i++) {
            JSONObject event = (JSONObject) events.get(i);
            String name = (String) event.get("name");
            String description = (String) event.get("description");
            String id = (String) event.get("id");
            JSONObject dates = (JSONObject) event.get("dates");
            JSONObject start = (JSONObject) dates.get("start");
            String date = (String) start.get("localDate");
            String time = (String) start.get("localTime");
            JSONObject _embedded = (JSONObject) event.get("_embedded");
            JSONArray venues = (JSONArray) _embedded.get("venues");
            JSONObject venue = (JSONObject) venues.get(0);
            String location = (String) venue.get("name");
            JSONArray priceRanges = (JSONArray) event.get("priceRanges");
            String price;
            if (priceRanges == null) {
                price = "N/A";
            } else {
                JSONObject priceRange = (JSONObject) priceRanges.get(0);
                if (priceRange == null) {
                    price = "N/A";
                } else {
                    price = Double.toString((Double) priceRange.get("min"));
                }

            }

            JSONArray images = (JSONArray) event.get("images");
            JSONObject image = (JSONObject) images.get(0);
            String imageUrl = (String) image.get("url");
            JSONObject locationObj = (JSONObject) venue.get("location");
            String latitude = (String) locationObj.get("latitude");
            String longitude = (String) locationObj.get("longitude");

            Event eventCurr = new Event(id, name, description, date, time, location, price, imageUrl, latitude, longitude);
            eventsList.add(eventCurr);


        }
        if (originFragment != null && classificationName.equals("")) {
            originFragment.setEventList(eventsList);
        } else {
            eventFragment.setEvents(eventsList);
        }
    }

    public void getCoordinates() {
        SharedPreferences sharedPreferences;
        if (originFragment != null) {
            sharedPreferences = originFragment.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        } else {
            sharedPreferences = eventFragment.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        }
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


    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    private String getFutureDate(int days) {
        String currentDate = getCurrentDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        date = c.getTime();
        String futureDate = sdf.format(date);
        return futureDate+"T23:59:59Z";

    }
}
