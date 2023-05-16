package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class Route {

    @SerializedName("destination")
    private String destination;
    @SerializedName("destination-id")
    private Integer destinationId;
    @SerializedName("distance")
    private String distance;
    @SerializedName("fare")
    private String fare;
    @SerializedName("source")
    private String source;
    @SerializedName("source-id")
    private Integer sourceId;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }
}
