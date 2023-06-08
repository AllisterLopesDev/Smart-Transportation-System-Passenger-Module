package com.example.sts_passenger.apiservices.request;

import com.google.gson.annotations.SerializedName;

public class UserProfileUpdateRequest {
    @SerializedName("contact")
    private String contact;
    @SerializedName("address")
    private String address;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
