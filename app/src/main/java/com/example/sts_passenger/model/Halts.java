package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class Halts {

    @SerializedName("id")
    private Integer id;

    @SerializedName("lat")
    private String latitude;

    @SerializedName("long")
    private String longitude;

    @SerializedName("name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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
