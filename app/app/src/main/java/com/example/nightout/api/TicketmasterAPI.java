package com.example.nightout.api;



import android.os.AsyncTask;
import android.util.Log;

import com.example.nightout.BuildConfig;
import com.example.nightout.ui.events.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class TicketmasterAPI {
//    private static final String API_KEY = BuildConfig.API_KEY;
//    private static Event[] events = new Event[5];
//
//    class apiTask extends AsyncTask<String,Void,Event[]>{
//        String TAG = getClass().getSimpleName();
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Log.d(TAG + " PreExceute","On pre Exceute......");
//        }
//
//        @Override
//        protected Event[] doInBackground(String... strings) {
//            Log.d(TAG + " DoINBackGround", "On doInBackground...");
//            try {
//                Event[] events = apiCall(strings[0],strings[1],strings[2],Integer.parseInt(strings[3]));
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return events;
//        }
//
//        @Override
//        protected void onPostExecute(Event[] events) {
//            super.onPostExecute(events);
//            Log.d(TAG + " onPostExecute", "" + events[0].getName());
//            TicketmasterAPI.events = events;
//        }
//
//        public Event[] apiCall(String keyword, String city , String state, int n) throws MalformedURLException, IOException, ParseException, JSONException {
//            String API_URL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + API_KEY;
//            URL url = new URL(API_URL + "&keyword=" + keyword + "&city=" + city + "&stateCode=" + state + "&size=" + n);
//            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//            Event[] eventCall = new Event[n];
//            int responseCode = connection.getResponseCode();
//            if (responseCode != 200) {
//                throw new RuntimeException("HttpResponseCode: " + responseCode);
//            } else {
//                String inline = "";
//                Scanner scanner = new Scanner(url.openStream());
//                while (scanner.hasNext()) {
//                    inline += scanner.nextLine();
//                }
//                scanner.close();
//                JSONParser parse = new JSONParser();
//                JSONObject data_obj = (JSONObject) parse.parse(inline);
//                JSONObject embedded = (JSONObject) data_obj.get("_embedded");
//                JSONArray events = (JSONArray) embedded.get("events");
//                for (int i = 0; i < events.length(); i++) {
//                    JSONObject event = (JSONObject) events.get(i);
//                    String name = (String) event.get("name");
//                    String description = (String) event.get("description");
//                    JSONObject dates = (JSONObject) event.get("dates");
//                    JSONObject start = (JSONObject) dates.get("start");
//                    String date = (String) start.get("localDate");
//                    String time = (String) start.get("localTime");
//                    JSONObject _embedded = (JSONObject) event.get("_embedded");
//                    JSONArray venues = (JSONArray) _embedded.get("venues");
//                    JSONObject venue = (JSONObject) venues.get(0);
//                    String location = (String) venue.get("name");
//                    JSONArray priceRanges = (JSONArray) event.get("priceRanges");
//                    JSONObject priceRange = (JSONObject) priceRanges.get(0);
//                    String price = (String) priceRange.get("min");
//                    JSONArray images = (JSONArray) event.get("images");
//                    JSONObject image = (JSONObject) images.get(0);
//                    String imageUrl = (String) image.get("url");
//                    Event eventCurr = new Event(name, description, date, time, location, price, imageUrl);
//                    eventCall[i] = eventCurr;
//                }
//            }
//        return eventCall;
//        }
//    }
//
//    public void searchEvents(String keyword, String city, String state,int n) {
//            new apiTask().execute(keyword,city,state,"" + n);
//        }
//
//
//
//
//    public static Event[] getEvents() {
//        return events;
//    }
//
//    public static void setEvents(Event[] events) {
//        TicketmasterAPI.events = events;
//    }
//
//    public static void setEvent(Event event, int index) {
//        TicketmasterAPI.events[index] = event;
//    }
//
//    public static Event getEvent(int index) {
//        return TicketmasterAPI.events[index];
//    }
//
//    public static void clearEvents() {
//        for (int i = 0; i < events.length; i++) {
//            events[i] = null;
//        }
//    }


}
