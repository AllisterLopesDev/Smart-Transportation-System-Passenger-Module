package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.model.User;
import com.google.gson.annotations.SerializedName;

public class LoginPassenger {
    private String message;
    private int status;
    private User user;

    @SerializedName("session")
    private Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public LoginPassenger(String message, int status, User user) {
        this.message = message;
        this.status = status;
        this.user = user;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
