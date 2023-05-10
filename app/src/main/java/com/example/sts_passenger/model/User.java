package com.example.sts_passenger.model;

public class User {
    private  String email;
    private  String token;
    private int userId;

    public User( int userId, String email, String token) {
        this.userId = userId;
        this.email = email;
        this.token = token;
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


    /*
    *
        "address": "Hno 41, quepem",
        "category": "student",
        "contact": "8805859397",
        "dob": "Mon, 16 Dec 1996 00:00:00 GMT",
        "firstname": "allister",
        "gender": "male",
        "lastname": "lopes"
    * */

    private String firstname;
    private String lastname;
    private String contact;
    private String address;
    private String dob;
    private String category;
    private String gender;

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
