package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class Schedule {


    private String arrival;
    private @SerializedName("arrival-stand") String arrivalStand;
    private String date;
    private String departure;
    private @SerializedName("departure-stand") String departureStand;
    private String duration;
    private Integer id;
    private @SerializedName("seats-available") Integer seatsAvailable;

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getArrivalStand() {
        return arrivalStand;
    }

    public void setArrivalStand(String arrivalStand) {
        this.arrivalStand = arrivalStand;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDepartureStand() {
        return departureStand;
    }

    public void setDepartureStand(String departureStand) {
        this.departureStand = departureStand;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }
}
