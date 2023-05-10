package com.example.sts_passenger.ticketbooking;

public class BookingDetails {
    private String source;
    private String destination;
    private String date;
    private String bookingId;

    public BookingDetails(String source, String destination, String date, String bookingId) {
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.bookingId = bookingId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }



    public String getBookingId() {
        return bookingId;
    }
}

