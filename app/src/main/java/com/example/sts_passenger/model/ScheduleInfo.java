package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class ScheduleInfo {

    @SerializedName("date")
    private String date;
    @SerializedName("id")
    private Integer id;
    @SerializedName("schedule")
    private Schedule schedule;
    @SerializedName("seats-available")
    private Integer seatsAvailable;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }
}
