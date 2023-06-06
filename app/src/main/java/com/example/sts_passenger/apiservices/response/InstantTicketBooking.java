package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.result.TicketBooking;
import com.google.gson.annotations.SerializedName;

public class InstantTicketBooking {
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private TicketBooking result;
    @SerializedName("status")
    private Integer status;
    @SerializedName("success")
    private Boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TicketBooking getResult() {
        return result;
    }

    public void setResult(TicketBooking result) {
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
