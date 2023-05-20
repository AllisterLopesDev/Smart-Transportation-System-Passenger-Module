package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class Session {
    @SerializedName("token")
    private String token;
    @SerializedName("passenger")
    private Passenger passenger;
    @SerializedName("user")
    private User user;


    // Session constructor
    public Session(User user, Passenger passenger, String token) {
        this.user = user;
        this.passenger = passenger;
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
