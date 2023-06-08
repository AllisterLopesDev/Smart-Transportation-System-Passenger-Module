package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.Passenger;
import com.google.gson.annotations.SerializedName;

public class UserProfileUpdateResponse {
//    {
//    "message": "Passenger details updated successfully",
//    "passenger": {
//        "address": "kevona",
//        "contact": "7768040595",
//        "dob": "Tue, 02 Jan 2001 00:00:00 GMT",
//        "firstname": "Karthik",
//        "gender": "MALE",
//        "id": 1,
//        "lastname": "phaldessai"
//    },
//    "status": 200,
//    "success": true
//}


    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private Integer status;
    @SerializedName("success")
    private Boolean success;
    @SerializedName("passenger")
    private Passenger passenger;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}
