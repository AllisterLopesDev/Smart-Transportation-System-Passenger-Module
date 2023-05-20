package com.example.sts_passenger.apiservices.response;

import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.model.User;
import com.google.gson.annotations.SerializedName;

public class LoginPassenger {
    private String message;
    private int status;
    @SerializedName("token")
    private String token;
    @SerializedName("session")
    private Session session;
//    private  User user;

//    public LoginPassenger(String message, int status, Session session) {
//        this.message = message;
//        this.status = status;
//        this.session = session;
//    }


    public LoginPassenger(String message, int status, String token, Session session) {
        this.message = message;
        this.status = status;
        this.token = token;
        this.session = session;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
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
