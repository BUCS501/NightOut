package com.example.nightout.api;

//import static com.example.nightout.BuildConfig.API_KEY;

import com.example.nightout.ui.events.DetailedEvent;
import com.example.nightout.ui.events.DetailedEventActivity;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DetailedTicketmasterRetrievalThread extends Thread {

    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events/";
    private final String id;
    private DetailedEventActivity originActivity;

    public DetailedTicketmasterRetrievalThread(DetailedEventActivity detailedEventActivity, String id) {
        this.originActivity = detailedEventActivity;
        this.id = id;
    }

    public DetailedTicketmasterRetrievalThread() {
        this.id = "Z7r9jZ1Ad_YkZ";
    }

    public void run() {
        try {
            getEvent(id);
        } catch (IOException | JSONException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    private void getEvent(String id) throws IOException, JSONException, org.json.simple.parser.ParseException {
        URL url = new URL(BASE_URL + id + "?apikey=" + "API_KEY");
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
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(content.toString());
        JSONObject embedded = (JSONObject) jsonObject.get("_embedded");
        String name = (String) jsonObject.get("name");
        JSONObject dates = (JSONObject) jsonObject.get("dates");
        JSONObject start = (JSONObject) dates.get("start");
        String description = (String) jsonObject.get("info");
        String date = (String) start.get("localDate");
        String time = (String) start.get("localTime");
        JSONArray classifications = (JSONArray) jsonObject.get("classifications");
        JSONObject classification = (JSONObject) classifications.get(0);
        JSONObject segment = (JSONObject) classification.get("segment");
        String genre = (String) segment.get("name");
        JSONArray venues = (JSONArray) embedded.get("venues");
        JSONObject venue = (JSONObject) venues.get(0);
        JSONObject addresses = (JSONObject) venue.get("address");
        String address = (String) addresses.get("line1");
        String venueName = (String) venue.get("name");
        JSONObject seatmaps = (JSONObject) jsonObject.get("seatmap");
        String seatmapurl = (String) seatmaps.get("staticUrl");
        String urlLink = (String) jsonObject.get("url");
        JSONArray images = (JSONArray) jsonObject.get("images");
        JSONObject image = (JSONObject) images.get(0);
        String imageUrl = (String) image.get("url");
        JSONArray priceRanges = (JSONArray) jsonObject.get("priceRanges");

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
        DetailedEvent detailedEvent = new DetailedEvent(id, name, description, date, time, venueName, price, imageUrl, seatmapurl, genre, urlLink,address);
        originActivity.setDetailedEvent(detailedEvent);



    }
}

