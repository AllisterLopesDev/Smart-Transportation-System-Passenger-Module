package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class TripHistory {

    @SerializedName("bus")
    private Bus bus;
    @SerializedName("schedule-info")
    private ScheduleInfo scheduleInfo;
    @SerializedName("ticket")
    private Ticket ticket;

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public ScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
