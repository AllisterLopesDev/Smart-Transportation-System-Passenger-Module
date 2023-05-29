package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.LiveLocation;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveLocationResponse {
//    @SerializedName("live_locations")
//    LiveLocation liveLocation;
    @SerializedName("status") private Integer status;
    @SerializedName("success") private Boolean success;

        @SerializedName("live_locations")
        private List<LiveLocation> live_locations;

        // Getters and setters


    public List<LiveLocation> getLive_locations() {
        return live_locations;
    }

    public void setLive_locations(List<LiveLocation> live_locations) {
        this.live_locations = live_locations;
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
