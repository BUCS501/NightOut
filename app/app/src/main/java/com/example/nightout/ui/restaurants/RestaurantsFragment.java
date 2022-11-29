package com.example.nightout.ui.restaurants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nightout.R;
import com.example.nightout.api.DetailedYelpRetrievalThread;
import com.example.nightout.api.ImageRetrievalThread;
import com.example.nightout.api.YelpRetrievalThread;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

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
        // create an executor service to run the thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Calls the Yelp API and sets the restaurants array to the results
        executor.execute(new YelpRetrievalThread(this));
        //executor.execute(new DetailedYelpRetrievalThread());
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        // terminate executor
        executor.shutdownNow();
        System.out.println();

    }

    @Override
    public void onStart() {
        super.onStart();
        lvRestaurants = (ListView) getView().findViewById(R.id.lvRestaurants);
        lvAdapter = new RestaurantAdapter(getActivity(), restaurants);
        lvRestaurants.setAdapter(lvAdapter);
        System.out.println();

        lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Restaurant restaurant = restaurants.get(i);
                Intent intent = new Intent(getActivity(), DetailedRestaurantActivity.class);
                intent.putExtra("restaurantID", restaurant.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}

class RestaurantAdapter extends BaseAdapter {

    private static final String TEST_ZIP = "02215";
    private static final String TEST_URL = "https://api.yelp.com/v3/businesses/search?term=restaurants&location=" + TEST_ZIP;
    private Context aContext;
    private ArrayList<Restaurant> restaurants;

    public RestaurantAdapter(Context aContext, ArrayList<Restaurant> restaurants) {
            this.aContext = aContext;
            this.restaurants = restaurants;
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
        rbRestaurant.setIsIndicator(true);
        restaurantName.setText(restaurants.get(i).getName());
        restaurantAddress.setText(restaurants.get(i).getFormattedAddress());
        rbRestaurant.setRating((float) restaurants.get(i).getRating());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new ImageRetrievalThread(restaurants.get(i).getImageUrl(), imgRestaurant));
        executor.shutdown();
        while (!executor.isTerminated()) {
            // wait for the thread to finish
        }
        executor.shutdownNow();
        return row;
    }
}