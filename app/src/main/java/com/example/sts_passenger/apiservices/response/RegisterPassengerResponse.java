package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.User;

public class RegisterPassengerResponse {

    /*
    * {
    "message": "passenger details registered successfully",
    "status": 200,
    "success": true,

}
    * */

    private String message;
    private int status;
    private  Boolean success;
    private User user;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

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



}