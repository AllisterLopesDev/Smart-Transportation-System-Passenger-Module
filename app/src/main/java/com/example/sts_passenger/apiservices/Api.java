package com.example.sts_passenger.apiservices;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.apiservices.request.RegisterPassengerRequest;
import com.example.sts_passenger.apiservices.request.RegisterUserRequest;
import com.example.sts_passenger.apiservices.request.RegistrationVerifyOtp;
import com.example.sts_passenger.apiservices.response.BusScheduleSearch;
import com.example.sts_passenger.apiservices.response.BusStops;
import com.example.sts_passenger.apiservices.response.InstantTicketBooking;
import com.example.sts_passenger.apiservices.response.LoginPassenger;
import com.example.sts_passenger.apiservices.request.LogoutPassenger;
import com.example.sts_passenger.apiservices.response.RegisterPassengerResponse;
import com.example.sts_passenger.apiservices.response.RegisterUserResponse;
import com.example.sts_passenger.apiservices.response.RegistrationOtp;
import com.example.sts_passenger.apiservices.response.SeatAvailability;
import com.example.sts_passenger.apiservices.response.TripHistoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @POST(Consts.ENDPOINT_REGISTRATION)
    Call<RegisterUserResponse> saveUser(@Body RegisterUserRequest userRequest);

    @POST(Consts.ENDPOINT_REQUEST_OTP)
    Call<RegistrationOtp> sendOtp(@Body com.example.sts_passenger.apiservices.request.RegistrationOtp requestOtp);

    @POST(Consts.ENDPOINT_VERIFY_OTP)
    Call<com.example.sts_passenger.apiservices.response.RegistrationVerifyOtp> verifyOtp(@Body RegistrationVerifyOtp requestVerifyOtp);

    @POST(Consts.ENDPOINT_LOGIN)
    Call<LoginPassenger> login(@Body com.example.sts_passenger.apiservices.request.LoginPassenger loginRequest);

    @HTTP(method = "DELETE", path = Consts.ENDPOINT_LOGOUT, hasBody = true)
    Call<com.example.sts_passenger.apiservices.response.LogoutPassenger> logout(@Body LogoutPassenger logoutRequest);

    @POST(Consts.ENDPOINT_ADD_PASSENGER_DETAILS)
    Call<RegisterPassengerResponse> addDetails(@Header ("Authorization") String token, @Body RegisterPassengerRequest registerPassenger);

    // api call for instant ticket
    @GET(Consts.ENDPOINT_SEARCH_BUS)
    Call<BusScheduleSearch> getAllAvailableBuses(@Query("source") Integer sourceId,
                                                 @Query("destination") Integer destinationId,
                                                 @Query("date") String date);

    // get halts
    @GET(Consts.ENDPOINT_BUS_STOPS)
    Call<BusStops> getBusStops();

    // check passenger count
    @POST(Consts.ENDPOINT_SEAT_AVAILABILITY)
    Call<SeatAvailability> checkSeatAvailability(@Query("bus-id") Integer busId,
                                                 @Query("passenger-count") Integer passengerCount,
                                                 @Query("date") String date,
                                                 @Query("schedule-info-id") Integer scheduleInfoId,
                                                 @Query("schedule-id") Integer scheduleId);

    @POST(Consts.ENDPOINT_BOOK_INSTANT_TICKET)
    Call<InstantTicketBooking> instantTicket(@Body com.example.sts_passenger.apiservices.request.InstantTicketBooking requestData);


    @GET(Consts.ENDPOINT_GET_TRIP_HISTORY)
    Call<TripHistoryResponse> getTripHistory(@Path("passenger_id") int id);

}
