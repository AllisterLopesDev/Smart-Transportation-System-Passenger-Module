package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.Passenger;
import com.example.sts_passenger.model.User;

public class RegisterPassenger {

    /*
    * {
    "message": "passenger details registered successfully",
    "status": 200,
    "success": true,

}
    * */

    private String message;
    private int status;
    private Passenger passenger;

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

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



    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}