package com.example.sts_passenger.apiservices.request;

import com.google.gson.annotations.SerializedName;

public class RegisterPassengerRequest {

    /*
    * User passenger registration and verification
    * {
    "firstname":"ali",
    "lastname":"lop",
    "contact":"8668173597",
    "address":"Hno 78/M, quepem, Goa 403720",
    "category":"student",
    "gender":"male",
    "dob":"1999-12-16",
    "userid": 6
}
    * */



    /*
    * To add user passenger details
    * */
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("contact")
    private String contact;
    @SerializedName("address")
    private String address;
    @SerializedName("category")
    private String category;
    @SerializedName("gender")
    private String gender;
    @SerializedName("dob")
    private String dob;
    @SerializedName("userid")
    private int userid;

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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setEmail(String userEmail) {
    }
}
