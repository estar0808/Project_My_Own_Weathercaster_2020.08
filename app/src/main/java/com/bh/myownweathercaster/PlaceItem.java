package com.bh.myownweathercaster;

public class PlaceItem {

    private String place;
    private String lat;
    private String lon;

    public String getPlace() {
        return place;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public PlaceItem(String place, String lat, String lon) {
        this.place = place;
        this.lat = lat;
        this.lon = lon;
    }
}
