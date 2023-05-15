package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.result.BusScheduleResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusScheduleSearch {

    @SerializedName("results")
    private List<BusScheduleResult> result;

    @SerializedName("success")
    private Boolean success;

    @SerializedName("status")
    private Integer status;


    public List<BusScheduleResult> getResult() {
        return result;
    }

    public void setResult(List<BusScheduleResult> result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
