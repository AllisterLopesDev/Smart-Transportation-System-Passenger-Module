package com.example.sts_passenger.model.result;

import com.google.gson.annotations.SerializedName;

public class PassDetails {

    @SerializedName("destination")
    private String destination;

    @SerializedName("id")
    private Integer id;

    @SerializedName("price")
    private Integer price;

    @SerializedName("source")
    private String source;

    @SerializedName("status")
    private String status;

    @SerializedName("valid-from")
    private String valid_from;

    @SerializedName("valid-to")
    private String valid_to;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }
}
