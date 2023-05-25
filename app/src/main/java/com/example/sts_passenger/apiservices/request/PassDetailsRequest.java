package com.example.sts_passenger.apiservices.request;

import com.google.gson.annotations.SerializedName;

public class PassDetailsRequest {
    //    "valid-from":"2023-5-19",
//    "valid-to":"2023-6-18",
//    "route-info-id": 1,
//    "price": 250

    @SerializedName("valid-from") String validDate;
    @SerializedName("valid-to") String validTill;
    @SerializedName("route-info-id") String routeInfoId;
    @SerializedName("price") String price;
    @SerializedName("date") String date;
    private Integer noOfDays;


    public Integer getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getValidTill() {
        return validTill;
    }

    public void setValidTill(String validTill) {
        this.validTill = validTill;
    }

    public String getRouteInfoId() {
        return routeInfoId;
    }

    public void setRouteInfoId(String routeInfoId) {
        this.routeInfoId = routeInfoId;
    }

    public String getPrice(String totalPassFaretoString) {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
