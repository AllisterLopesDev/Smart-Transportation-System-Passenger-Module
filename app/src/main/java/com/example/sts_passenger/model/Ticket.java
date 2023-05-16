package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class Ticket {
    @SerializedName("booked-at")
    private String date;
    @SerializedName("destination")
    private String destination;
    @SerializedName("fare-amount")
    private Integer fareAmount;
    @SerializedName("id")
    private Integer id;
    @SerializedName("passenger-count")
    private Integer passengerCount;
    @SerializedName("source")
    private String source;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(Integer fareAmount) {
        this.fareAmount = fareAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
