package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.result.PassDetails;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassengerPassDetailsResponse {

@SerializedName("result")
 private List<PassDetails> result;

@SerializedName("status")
    private Integer status;

@SerializedName("success")
private boolean success;

    public List<PassDetails> getResult() {
        return result;
    }

    public void setResult(List<PassDetails> result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
