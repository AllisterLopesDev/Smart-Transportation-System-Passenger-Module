package com.example.sts_passenger.ticketbooking;

import com.google.gson.annotations.SerializedName;

public class BusStops {
    @SerializedName("id") int id;
    @SerializedName("lat") String lat;
    @SerializedName("long") String longitude;
    @SerializedName("name") String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
