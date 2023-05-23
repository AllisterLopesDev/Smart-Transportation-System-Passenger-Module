package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.TripHistory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripHistoryResponse {
    @SerializedName("bookings")
    List<TripHistory> bookings;
    @SerializedName("passenger_id")
    Integer passengerId;
    @SerializedName("status")
    Integer status;
    @SerializedName("success")
    Boolean success;

    public List<TripHistory> getBookings() {
        return bookings;
    }

    public void setBookings(List<TripHistory> bookings) {
        this.bookings = bookings;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
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
}
