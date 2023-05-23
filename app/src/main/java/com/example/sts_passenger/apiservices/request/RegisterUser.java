package com.example.sts_passenger.apiservices.request;

import com.google.gson.annotations.SerializedName;

public class RegisterUser {

    @SerializedName("email") String email;
    @SerializedName("password") String password;
    String ipAddress;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
