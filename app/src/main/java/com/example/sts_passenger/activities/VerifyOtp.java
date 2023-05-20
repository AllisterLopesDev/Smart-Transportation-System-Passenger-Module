package com.example.sts_passenger.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;

import com.example.sts_passenger.apiservices.response.RegisterUser;
import com.example.sts_passenger.sharedpref.SharedPrefManager;
import com.example.sts_passenger.apiservices.response.RegistrationVerifyOtp;

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
//                register(createRequest());
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


    public com.example.sts_passenger.apiservices.request.RegistrationVerifyOtp createVerifyRequest() {
        com.example.sts_passenger.apiservices.request.RegistrationVerifyOtp requestVerifyOtp = new com.example.sts_passenger.apiservices.request.RegistrationVerifyOtp();
        requestVerifyOtp.setEmail(getUserEmail());
        requestVerifyOtp.setOtp(otp_code.getText().toString());
        return requestVerifyOtp;
    }

    public void verifyOtp(com.example.sts_passenger.apiservices.request.RegistrationVerifyOtp requestVerifyOtp) {
        Call<RegistrationVerifyOtp> registrationVerifyOtpCall = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().verifyOtp(requestVerifyOtp);
        registrationVerifyOtpCall.enqueue(new Callback<RegistrationVerifyOtp>() {
            @Override
            public void onResponse(Call<RegistrationVerifyOtp> call, Response<RegistrationVerifyOtp> response) {
                try {
                    Log.i("TAG", "Response code: " + response.code());
                    Log.i("TAG", "Response body: " + response.body());

                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getStatus() == 200) {
                            Log.i("TAG", "onResponse: otp " + response.body().getMessage());
                            register(createRequest());
                        } else {
                            Log.i("TAG", "onResponse: Invalid status code or response body is null");
                        }
                    } else {
                        Log.i("TAG", "onResponse: NOT SUCCESS");
                    }
                } catch (Exception e) {
                    Log.i("TAG", "exception e: " + e);
                }
            }

            @Override
            public void onFailure(Call<RegistrationVerifyOtp> call, Throwable t) {
                Log.i("TAG", "onFailure: otp "+t.getLocalizedMessage());
            }
        });

    }



    // ---------------------------------------REGISTER User ----------------------------- //


    public com.example.sts_passenger.apiservices.request.RegisterUser createRequest() {
        com.example.sts_passenger.apiservices.request.RegisterUser userRequest = new com.example.sts_passenger.apiservices.request.RegisterUser();
        userRequest.setEmail(getUserEmail());
        userRequest.setPassword(getUserPassword());
        userRequest.setIpAddress(getIpAddress());
        return userRequest;
    }


    public void register(com.example.sts_passenger.apiservices.request.RegisterUser userRequest) {
        Call<RegisterUser> userCall = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().saveUser(userRequest);
        userCall.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if (response.isSuccessful()){
                    if (response.body() != null && response.body().getStatus() == 200 ){
                        Toast.makeText(VerifyOtp.this, "user registered " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        sharedPrefManager.saveUser(response.body().getUser());
                        Intent i = new Intent(getApplicationContext(), User_register_details.class);
                        startActivity(i);
                    }
                }else {
                    Log.i("TAG", "onResponse: NOT SUCCESS");
                }
            }

            @Override

            public void onFailure(Call<RegisterUser> call, Throwable t) {
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