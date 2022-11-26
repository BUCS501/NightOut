package com.example.nightout.ui.restaurants;

public class Hours implements Comparable<Hours> {

    private long day;
    private String open;
    private String close;

    public Hours(long day, String open, String close) {
        this.day = day;
        this.open = open;
        this.close = close;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    @Override
    public int compareTo(Hours other) {
        return Long.compare(this.day, other.day);
    }
}
