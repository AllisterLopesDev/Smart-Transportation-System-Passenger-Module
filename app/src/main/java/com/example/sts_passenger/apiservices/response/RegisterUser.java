package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.User;
import com.google.gson.annotations.SerializedName;

public class RegisterUser {

    @SerializedName("message") String message;
    @SerializedName("status") Integer status;
    @SerializedName("success") Boolean success;
    @SerializedName("user")
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
