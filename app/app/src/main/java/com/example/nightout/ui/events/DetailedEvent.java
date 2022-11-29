package com.example.nightout.ui.events;

public class DetailedEvent extends Event{
private String seatMapUrl;
private String segment;
private String eventUrl;
private String address;



    public DetailedEvent(String id,String name, String description, String date, String time, String location, String price, String image, String seatMapUrl, String segment, String eventUrl,String address) {
        super(id,name, description, date, time, location, price, image);
        this.seatMapUrl = seatMapUrl;
        this.segment = segment;
        this.eventUrl = eventUrl;
        this.address = address;

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
}
