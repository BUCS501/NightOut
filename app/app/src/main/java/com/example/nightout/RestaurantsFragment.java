package com.example.nightout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.*;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ListView lvRestaurants;
    private ListAdapter lvAdapter;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Restaurants.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantsFragment newInstance(String param1, String param2) {
        RestaurantsFragment fragment = new RestaurantsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lvRestaurants = (ListView) getView().findViewById(R.id.lvRestaurants);
        try {
            lvAdapter = new RestaurantAdapter(getActivity());
        } catch (IOException | ParseException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }
}

class RestaurantAdapter extends BaseAdapter {

    private static final String TEST_ZIP = "02215";
    private static final String TEST_URL = "https://api.yelp.com/v3/businesses/search?term=restaurants&location=" + TEST_ZIP;
    private Context aContext;
    private ArrayList<Restaurant> restaurants;

    public RestaurantAdapter(Context aContext) throws IOException, ParseException, JSONException {
            this.aContext = aContext;
            getRestaurants();
    }

    public void getRestaurants() throws IOException, ParseException, JSONException {
        restaurants = new ArrayList<Restaurant>();
        URL url = new URL(TEST_URL);
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
        JSONObject obj = (JSONObject) new JSONParser().parse(result);
        JSONArray arr = (JSONArray) obj.get("businesses");
        for (int i = 0; i < arr.length(); i++) {
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

        in.close();

    }


    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurants.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // sourced from outside resource
    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            row = LayoutInflater.from(aContext).inflate(R.layout.listview_row, parent, false);
        } else {
            row = convertView;
        }
        ImageView imgRestaurant = (ImageView) row.findViewById(R.id.imgRestaurant);
        TextView restaurantName = (TextView) row.findViewById(R.id.restaurantName);
        TextView restaurantAddress = (TextView) row.findViewById(R.id.restaurantAddress);
        RatingBar rbRestaurant = (RatingBar) row.findViewById(R.id.rbRestaurant);

        imgRestaurant.setImageBitmap(getBitmapFromURL(restaurants.get(i).getImageUrl()));
        restaurantName.setText(restaurants.get(i).getName());
        restaurantAddress.setText(restaurants.get(i).getFormattedAddress());
        rbRestaurant.setRating((float) restaurants.get(i).getRating());

        return row;
    }
}