package com.example.sts_passenger;

public class Consts {



    public static final String IP_ADDRESS = "3.110.42.226";


//    public static final String IP_ADDRESS = "192.168.169.107:5000";

    // Base URLs
    public static final String BASE_URL_PASSENGER_AUTH = "http://"+IP_ADDRESS+"/user/";
    public static final String BASE_URL_BOOKING = "http://"+IP_ADDRESS+"/booking/";
    public static final String BASE_URL_SCHEDULE = "http://"+IP_ADDRESS+"/schedule/";



    public static final String BASE_URL_BUS = "http://"+IP_ADDRESS+"/bus/";

    // Endpoints
    public static final String ENDPOINT_LOGIN = "login";
    public static final String ENDPOINT_LOGOUT = "logout";
    public static final String ENDPOINT_REGISTRATION = "register";
    public static final String ENDPOINT_REQUEST_OTP = "send_otp";
    public static final String ENDPOINT_VERIFY_OTP = "verify_otp";
    public static final String ENDPOINT_BUS_STOPS = "bus-stops";
    public static final String ENDPOINT_ADD_PASSENGER_DETAILS = "add-passenger-details";

    public static final String ENDPOINT_SEAT_AVAILABILITY = "seat-available";
    public static final String ENDPOINT_PASSENGER_PASS_DETAILS = "passenger/{passenger_id}/passes";

    // booking endpoints
    public static final String ENDPOINT_BOOK_INSTANT_TICKET = "instant";
    public static final String ENDPOINT_GET_TICKET_BOOKING = "passenger-ticket-bookings";


    // Endpoints for Bus
    public static final String ENDPOINT_SEARCH = "search";


        public static final String ENDPOINT_SEARCH_BUS = "search";


        // booking
        public static final String ENDPOINT_GET_TRIP_HISTORY = "passenger-ticket-bookings/{passenger_id}";



    // Request parameters
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_FIRST_NAME = "first_name";
    public static final String PARAM_LAST_NAME = "last_name";
    public static final String PARAM_ADDRESS = "address";
    public static final String PARAM_PHONE_NUMBER = "phone_number";



    // location
    public static final Integer LOCATION_THRESHOLD = 100;

    // button

    public static final String  BUTTON_SEARCH_BUS = "Search buses";

    // request codes
    public static final Integer LOCATION_REQUEST_CODE = 1;
}
