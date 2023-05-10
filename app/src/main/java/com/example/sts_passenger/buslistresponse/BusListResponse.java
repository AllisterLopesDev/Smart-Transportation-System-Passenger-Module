package com.example.sts_passenger.buslistresponse;

import com.example.sts_passenger.searchbuses.Results;

import java.util.List;

public class BusListResponse {
    List<Results> results;
    Integer status;
    boolean success;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
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
