package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.User;

public class RegisterUserResponse {
    private String message;
    private Integer status;

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
}
