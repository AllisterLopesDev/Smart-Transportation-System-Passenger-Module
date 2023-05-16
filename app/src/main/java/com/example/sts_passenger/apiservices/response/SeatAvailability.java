package com.example.sts_passenger.apiservices.response;

import com.google.gson.annotations.SerializedName;

public class SeatAvailability {
    @SerializedName("available-seats")
    private Integer availableSeats;
    @SerializedName("status")
    private Integer status;
    @SerializedName("success")
    private Boolean success;

    @SerializedName("message")
    private String message;

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
