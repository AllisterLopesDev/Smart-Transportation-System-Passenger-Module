package com.example.sts_passenger;

import com.example.sts_passenger.buslistresponse.BusListResponse;
import com.example.sts_passenger.login.LoginRequest;
import com.example.sts_passenger.login.LoginResponse;
import com.example.sts_passenger.logout.LogoutRequest;
import com.example.sts_passenger.logout.LogoutResponse;
import com.example.sts_passenger.register.UserRequest;
import com.example.sts_passenger.register.UserResponse;
import com.example.sts_passenger.send_otp.RequestOtp;
import com.example.sts_passenger.send_otp.ResponseOtp;
import com.example.sts_passenger.ticketbooking.ResponseBusStrand;
import com.example.sts_passenger.verify_otp.RequestVerifyOtp;
import com.example.sts_passenger.verify_otp.ResponseVerifyOtp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST(Consts.ENDPOINT_REGISTRATION)
    Call<UserResponse> saveUser(@Body UserRequest userRequest);

    @POST(Consts.ENDPOINT_REQUEST_OTP)
    Call<ResponseOtp> sendOtp(@Body RequestOtp requestOtp);

    @POST(Consts.ENDPOINT_VERIFY_OTP)
    Call<ResponseVerifyOtp> verifyOtp(@Body RequestVerifyOtp requestVerifyOtp);

    @POST(Consts.ENDPOINT_LOGIN)
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @HTTP(method = "DELETE", path = Consts.ENDPOINT_LOGOUT, hasBody = true)
    Call<LogoutResponse> logout(@Body LogoutRequest logoutRequest);

    @POST(Consts.ENDPOINT_ADD_PASSENGER_DEATILS)
    Call<UserResponse> addDetails(@Header ("Authorization") String token, @Body UserRequest userRequest);

    @GET(Consts.ENDPOINT_BUS_STOPS)
    Call<ResponseBusStrand> getBusStands();

    @GET(Consts.ENDPOINT_SEARCH_BUS)
    Call<BusListResponse> getBusList(
            @Query("source") Integer source,
            @Query("destination") Integer destination,
            @Query("date") String date
    );

}
