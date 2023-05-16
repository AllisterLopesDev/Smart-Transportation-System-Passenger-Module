package com.example.sts_passenger.apiservices.request;

import com.google.gson.annotations.SerializedName;

public class InstantTicketBooking {
    @SerializedName("booked-at")
    private String bookingDate;
    @SerializedName("total-fare-amount")
    private Double totalFareAmount;
    @SerializedName("distance-travelled")
    private String distance;
    @SerializedName("passenger-count")
    private Integer passengerCount;
    @SerializedName("source-id")
    private Integer sourceId;
    @SerializedName("destination-id")
    private Integer destinationId;
    @SerializedName("passenger-id")
    private Integer passengerId;
    @SerializedName("bus-schedule-id")
    private Integer busScheduleId;

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Double getTotalFareAmount() {
        return totalFareAmount;
    }

    public void setTotalFareAmount(Double totalFareAmount) {
        this.totalFareAmount = totalFareAmount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    public Integer getBusScheduleId() {
        return busScheduleId;
    }

    public void setBusScheduleId(Integer busScheduleId) {
        this.busScheduleId = busScheduleId;
    }
}
