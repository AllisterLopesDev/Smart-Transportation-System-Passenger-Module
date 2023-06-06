package com.example.sts_passenger.apiservices.response;

import com.google.gson.annotations.SerializedName;

public class PassDetailsResponse {

//    "message": "Pass created successfully",
//    "pass_id": 17,
//    "status": 200,
//    "success": true

    @SerializedName("message") private String message;
    @SerializedName("pass_id") private Integer pass_id;
    @SerializedName("status") private Integer status;
    @SerializedName("success") private Boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPass_id() {
        return pass_id;
    }

    public void setPass_id(Integer pass_id) {
        this.pass_id = pass_id;
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
