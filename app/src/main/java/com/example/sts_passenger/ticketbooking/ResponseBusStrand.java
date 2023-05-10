package com.example.sts_passenger.ticketbooking;

import java.util.List;

public class ResponseBusStrand {

    List<BusStops> result;
    int status;
    boolean success;

    public List<BusStops> getResult() {
        return result;
    }

    public void setResult(List<BusStops> result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
