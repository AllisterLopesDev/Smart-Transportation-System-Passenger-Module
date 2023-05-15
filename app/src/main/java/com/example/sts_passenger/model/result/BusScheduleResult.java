package com.example.sts_passenger.model.result;

import com.example.sts_passenger.model.Bus;
import com.example.sts_passenger.model.Route;
import com.example.sts_passenger.model.Schedule;
import com.google.gson.annotations.SerializedName;

public class BusScheduleResult {
    @SerializedName("bus")
    Bus bus;
    @SerializedName("route")
    Route route;
    @SerializedName("schedule")
    Schedule schedule;

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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
