package com.example.nightout.ui.restaurants;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailedRestaurant extends Restaurant {

    private String phoneNumber;
    private String displayedPhoneNumber;
    private String restaurantUrl;
    private ArrayList<Hours> weekHours;

    public DetailedRestaurant(String id, String name, String address, String city, String state, String zip, String price, String imageUrl, double rating) {
        super(id, name, address, city, state, zip, price, imageUrl, rating);
    }

    public DetailedRestaurant(String id, String name, String address, String city, String state, String zip, String price, String imageUrl, double rating, String phoneNumber, String displayedPhoneNumber, String restaurantUrl, ArrayList<Hours> weekHours) {
        super(id, name, address, city, state, zip, price, imageUrl, rating);
        this.phoneNumber = phoneNumber;
        this.displayedPhoneNumber = displayedPhoneNumber;
        this.restaurantUrl = restaurantUrl;
        this.weekHours = weekHours;
    }

    public DetailedRestaurant(Restaurant restaurant, String phoneNumber, String displayedPhoneNumber, String restaurantUrl, ArrayList<Hours> weekHours) {
        super(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getCity(), restaurant.getState(), restaurant.getZip(), restaurant.getPrice(), restaurant.getImageUrl(), restaurant.getRating());
        this.phoneNumber = phoneNumber;
        this.displayedPhoneNumber = displayedPhoneNumber;
        this.restaurantUrl = restaurantUrl;
        this.weekHours = weekHours;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayedPhoneNumber() {
        return displayedPhoneNumber;
    }

    public void setDisplayedPhoneNumber(String displayedPhoneNumber) {
        this.displayedPhoneNumber = displayedPhoneNumber;
    }

    public String getRestaurantUrl() {
        return restaurantUrl;
    }

    public void setRestaurantUrl(String restaurantUrl) {
        this.restaurantUrl = restaurantUrl;
    }

    public ArrayList<Hours> getWeekHours() {
        return weekHours;
    }

    public void setWeekHours(ArrayList<Hours> weekHours) {
        this.weekHours = weekHours;
    }

    public boolean isOpen() {
        // TODO: Implement this method if time permits, current struggle is with API version being too low (21 when 26 is required)
        return true;
    }

}
