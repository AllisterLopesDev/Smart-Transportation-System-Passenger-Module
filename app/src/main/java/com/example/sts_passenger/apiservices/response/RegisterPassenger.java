package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.Passenger;
import com.example.sts_passenger.model.User;

public class RegisterPassenger {

    /*
    * "message": "user already registered",
    * "status": "400"
    * */

    private String message;
    private int status;
    private User user;
    private Passenger passenger;

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
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



    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}