package com.example.sts_passenger;

public class Consts {

    // Base URLs
    public static final String BASE_URL_PASSENGER_AUTH = "http://3.110.42.226/user/";
    public static final String BASE_URL_BOOKING = "http://3.110.42.226/booking/";
    public static final String BASE_URL_BUS = "http://3.110.42.226/bus/";

    // Endpoints
    public static final String ENDPOINT_LOGIN = "login";
    public static final String ENDPOINT_LOGOUT = "logout";
    public static final String ENDPOINT_REGISTRATION = "registration";
    public static final String ENDPOINT_REQUEST_OTP = "send_otp";
    public static final String ENDPOINT_VERIFY_OTP = "verify_otp";
    public static final String ENDPOINT_BUS_STOPS = "bus-stops";
    public static final String ENDPOINT_ADD_PASSENGER_DETAILS = "add-passenger-details";




    // Endpoints for Bus
    public static final String ENDPOINT_SEARCH_BUS = "search";


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
