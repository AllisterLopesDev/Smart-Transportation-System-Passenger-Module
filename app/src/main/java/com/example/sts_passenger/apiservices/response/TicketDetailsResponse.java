package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.result.TicketBooking;
import com.example.sts_passenger.model.result.TicketResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TicketDetailsResponse {



    @SerializedName("bookings") List<TicketBooking> ticketBookingList;
    @SerializedName("results") List<TicketResult> ticketResultList;
    @SerializedName("passenger_id") Integer passenger_id;
    @SerializedName("status") Integer status;
    @SerializedName("success") Boolean success;

    public List<TicketResult> getTicketResultList() {
        return ticketResultList;
    }

    public void setTicketResultList(List<TicketResult> ticketResultList) {
        this.ticketResultList = ticketResultList;
    }

    public List<TicketBooking> getTicketBookingList() {
        return ticketBookingList;
    }

    public void setTicketBookingList(List<TicketBooking> ticketBookingList) {
        this.ticketBookingList = ticketBookingList;
    }

    public Integer getPassenger_id() {
        return passenger_id;
    }

    public void setPassenger_id(Integer passenger_id) {
        this.passenger_id = passenger_id;
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
