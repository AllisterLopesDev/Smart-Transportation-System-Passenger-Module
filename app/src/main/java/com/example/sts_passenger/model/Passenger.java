package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class Passenger {

    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("contact")
    private String contact;
    @SerializedName("address")
    private String address;
    @SerializedName("dob")
    private String dob;
    @SerializedName("category")
    private String category;
    @SerializedName("gender")
    private String gender;

//    "userid": 7

    @SerializedName("id")
    private Integer passengerId;

    // for saving photo details
    @SerializedName("file-name")
    private String fileName;
    private String urlPath;

    public Passenger(String firstname, String lastname, String contact, String address, String dob, String category, String gender, Integer passengerId, String fileName) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.contact = contact;
        this.address = address;
        this.dob = dob;
        this.category = category;
        this.gender = gender;
        this.passengerId = passengerId;
        this.fileName = fileName;
    }

    public Passenger() {}



    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }
}
