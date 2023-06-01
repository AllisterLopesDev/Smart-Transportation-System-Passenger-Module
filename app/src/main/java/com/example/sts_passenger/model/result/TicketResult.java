package com.example.sts_passenger.model.result;

import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.ScheduleInfo;
import com.example.sts_passenger.model.Ticket;
import com.google.gson.annotations.SerializedName;

public class TicketResult {
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
