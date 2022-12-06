package com.example.nightout.ui.events;

public class Event {
    private String id;
    private String name;
    private String description;
    private String date;
    private String time;
    private String location;
    private String price;
    private String image;
    private String latitude;
    private String longitude;

    public Event(String id, String name, String description, String date, String time, String location, String price, String image, String latitude,String longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.price = price;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
