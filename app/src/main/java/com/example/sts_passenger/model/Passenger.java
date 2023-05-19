package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class Passenger {

    private String firstname;
    private String lastname;
    private String contact;
    private String address;
    private String dob;
    private String category;
    private String gender;
    @SerializedName("id") Integer id;

    public Passenger(String firstname, String lastname, String contact, String address, String dob, String category, String gender, Integer id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.contact = contact;
        this.address = address;
        this.dob = dob;
        this.category = category;
        this.gender = gender;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
