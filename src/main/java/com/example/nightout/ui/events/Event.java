package com.example.nightout.ui.events;

public class Event {
    private String name;
    private String description;
    private String date;
    private String time;
    private String location;
    private String price;
    private String image;

    public Event(String name, String description, String date, String time, String location, String price, String image) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.price = price;
        this.image = image;
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
}
