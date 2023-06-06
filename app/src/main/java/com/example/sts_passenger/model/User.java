package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("email")
    private  String email;
    private  String token;

    @SerializedName("id") private int userId;

    public User( int userId, String email, String token) {
        this.userId = userId;
        this.email = email;
        this.token = token;
    }

    public User(int id, String email) {
        this.userId = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
