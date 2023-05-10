package com.example.sts_passenger.register;

import com.example.sts_passenger.model.User;

public class UserResponse {

    /*
    * "message": "user already registered",
    * "status": "400"
    * */

    private String message;
    private int status;

    private User user;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    /*
    * {
    "message": "passenger details registered successfully",
    "status": 200,
    "success": true,
    "user": {
        "address": "Hno 41, quepem",
        "category": "student",
        "contact": "8805859397",
        "dob": "Mon, 16 Dec 1996 00:00:00 GMT",
        "firstname": "allister",
        "gender": "male",
        "lastname": "lopes"
    }
}
    * */
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}