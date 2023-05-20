package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.RouteInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteInfoResponse {
    @SerializedName("result")
    List<RouteInfo> result;
    private Integer status;
    private Boolean success;

    public List<RouteInfo> getResult() {
        return result;
    }

    public void setResult(List<RouteInfo> result) {
        this.result = result;
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
