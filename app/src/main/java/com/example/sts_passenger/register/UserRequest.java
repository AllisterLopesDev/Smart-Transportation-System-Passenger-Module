package com.example.sts_passenger.register;

public class UserRequest {

    /*
    * User passenger registration and verification
    * */
    private String email;
    private String password;
    private String ipAddress;

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

    /* {
    "firstname":"darron",
    "lastname":"moraes",
    "contact":"8668713597",
    "address":"Hno 789/M, Talconda-Caveri, Raia, Salcete, Goa 403720",
    "category":"student",
    "gender":"male",
    "dob":"1996-12-14",
    "userid": 6
}*/


    /*
    * To add user passenger details
    * */
    private String firstname;
    private String lastname;
    private String contact;
    private String address;
    private String category;
    private String gender;
    private String dob;
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
}
