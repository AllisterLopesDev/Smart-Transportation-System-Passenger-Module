package com.example.sts_passenger.model;

import com.google.gson.annotations.SerializedName;

public class RouteInfo {
//    {
//            "destination": {
//                "id": 1,
//                "name": "MARGAO"
//            },
//            "distance": "25",
//            "fare": "25",
//            "id": 5,
//            "source": {
//                "id": 7,
//                "name": "DARRON"
//            },
//            "type": "SHUTTLE"
//        },

    @SerializedName("destination")
    private Halts destination;

    @SerializedName("distance")
    String distance;
    @SerializedName("fare")
    String fare;
    @SerializedName("id")
    Integer id;
    @SerializedName("type")
    String type;
    @SerializedName("source")
    private Halts source;

    public Halts getDestination() {
        return destination;
    }

    public void setDestination(Halts destination) {
        this.destination = destination;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Halts getSource() {
        return source;
    }

    public void setSource(Halts source) {
        this.source = source;
    }
}
