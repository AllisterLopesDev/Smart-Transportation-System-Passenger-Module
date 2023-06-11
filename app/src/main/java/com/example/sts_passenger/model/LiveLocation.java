package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class LiveLocation {

//            "bus_schedule_id": 34,
//                    "latitude": 15.345,
//                    "longitude": 74.0015
    @SerializedName("bus_schedule_id") private Integer busScheduleId;
    @SerializedName("latitude") private Double latitude;
    @SerializedName("longitude") private Double longitude;

    public Integer getBusScheduleId() {
        return busScheduleId;
    }

    public void setBusScheduleId(Integer busScheduleId) {
        this.busScheduleId = busScheduleId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
