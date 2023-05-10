package com.example.sts_passenger.searchbuses;

import com.google.gson.annotations.SerializedName;

public class Bus {


    private Integer id;
    private @SerializedName("reg-no") String regNo;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
