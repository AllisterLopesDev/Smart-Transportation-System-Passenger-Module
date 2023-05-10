package com.example.sts_passenger.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.register.UserRequest;
import com.example.sts_passenger.register.UserResponse;
import com.example.sts_passenger.sharedpref.SharedPrefManager;
import com.example.sts_passenger.verify_otp.RequestVerifyOtp;
import com.example.sts_passenger.verify_otp.ResponseVerifyOtp;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtp extends AppCompatActivity {

    TextView tv1;
    EditText otp_code;
    Button verifyBtn;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        initViews();

        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        tv1.setText(getUserEmail());
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp(createVerifyRequest());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sharedPrefManager.isLogged()){
            Intent i = new Intent(getApplicationContext(), PassengerHomePage.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    public  void initViews(){
        tv1  = findViewById(R.id.email);
        otp_code = findViewById(R.id.otp);
        verifyBtn = findViewById(R.id.verifybtn);
    }



    public String getUserEmail() {
        Intent i = getIntent();
        return i.getStringExtra("email");
    }

    public String getUserPassword() {
        Intent i = getIntent();
        return i.getStringExtra("password");
    }


    // ---------------------------- Verify OTP ----------------------------------------- //


    public RequestVerifyOtp createVerifyRequest() {
        RequestVerifyOtp requestVerifyOtp = new RequestVerifyOtp();
        requestVerifyOtp.setEmail(getUserEmail());
        requestVerifyOtp.setOtp(otp_code.getText().toString());

        return requestVerifyOtp;
    }

    public void verifyOtp(RequestVerifyOtp requestVerifyOtp) {
        Call<ResponseVerifyOtp> responseVerifyOtpCall = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().verifyOtp(requestVerifyOtp);
        responseVerifyOtpCall.enqueue(new Callback<ResponseVerifyOtp>() {
            @Override
            public void onResponse(Call<ResponseVerifyOtp> call, Response<ResponseVerifyOtp> response) {

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Toast.makeText(VerifyOtp.this, "Verified OTP ", Toast.LENGTH_SHORT).show();

                        register(createRequest());

                    } else {
                        Toast.makeText(VerifyOtp.this, "request failed"+ response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }



            @Override
            public void onFailure(Call<ResponseVerifyOtp> call, Throwable t) {
                Toast.makeText(VerifyOtp.this, "failed " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    // ---------------------------------------REGISTER User ----------------------------- //

    public UserRequest createRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(getUserEmail());
        userRequest.setPassword(getUserPassword());
//        userRequest.setIpAddress(getIpAddress());
        return userRequest;
    }

    public void register(UserRequest userRequest) {
        Call<UserResponse> userResponseCall = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (response.isSuccessful()) {
                    if (userResponse.getStatus() == 200) {
                        sharedPrefManager.saveUser(userResponse.getUser());
                        Toast.makeText(VerifyOtp.this, "user registered " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), User_register_details.class);
                        startActivity(i);
                    }
                }else {
                        Toast.makeText(VerifyOtp.this, "request failed"+ response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(VerifyOtp.this, "failed " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    // ------------------- IP Address ---------------------------------//

    public String getIpAddress(){
        String ipAddress = "";
        try {
            // get list of all network interfaces
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                // get list of all ip addresses assigned
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // return the ip address as a string
                        ipAddress = inetAddress.getHostAddress();
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
//                e.printStackTrace();
        }
        return ipAddress;
    }


}