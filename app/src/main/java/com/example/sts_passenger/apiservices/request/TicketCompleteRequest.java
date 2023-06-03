package com.example.sts_passenger.apiservices.request;

import com.google.gson.annotations.SerializedName;

public class TicketCompleteRequest {
//    {
//    "ticket-id": 29,
//    "halt-id": 1,
//    "passenger-count": 5
//}

    @SerializedName("ticket-id")
    private Integer ticketId;
    @SerializedName("halt-id")
    private Integer haltId;
    @SerializedName("passenger-count")
    private Integer passengerCount;

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getHaltId() {
        return haltId;
    }

    public void setHaltId(Integer haltId) {
        this.haltId = haltId;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }
}
