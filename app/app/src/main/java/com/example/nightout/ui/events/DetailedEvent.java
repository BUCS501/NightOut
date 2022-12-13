package com.example.nightout.ui.events;

public class DetailedEvent extends Event{
private String seatMapUrl;
private String segment;
private String eventUrl;
private String address;
private String minPrice;
private String maxPrice;
private String currency;



    public DetailedEvent(String id,String name, String description, String date, String time, String location, String price, String image, String seatMapUrl, String segment, String eventUrl,String address,String latitude,String longitude, String minPrice, String maxPrice, String currency) {
        super(id,name, description, date, time, location, price, image,latitude,longitude);
        this.seatMapUrl = seatMapUrl;
        this.segment = segment;
        this.eventUrl = eventUrl;
        this.address = address;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.currency = currency;


    }

    public String getSeatMapUrl() {
        return seatMapUrl;


    }

    public String getSegment() {
        return segment;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public String getAddress() {
        return address;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPriceRange() {
        if (minPrice.equals("N/A") || maxPrice.equals("N/A")) {
            return null;
        }else if (minPrice.equals(maxPrice)) {
            return minPrice + " " + currency;
        }


        return minPrice + " - " + maxPrice + " " + currency;
    }


}
