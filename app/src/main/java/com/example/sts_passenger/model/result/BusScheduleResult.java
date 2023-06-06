package com.example.sts_passenger.model.result;

import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.Route;
import com.example.sts_passenger.model.Schedule;
import com.example.sts_passenger.model.ScheduleInfo;
import com.google.gson.annotations.SerializedName;

public class BusScheduleResult {
    @SerializedName("bus")
    private Bus bus;
    @SerializedName("route-info")
    private Route route;
    @SerializedName("schedule-info")
    private ScheduleInfo scheduleInfo;

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public ScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }
}
