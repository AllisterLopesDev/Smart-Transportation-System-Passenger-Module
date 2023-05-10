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
import com.example.sts_passenger.login.LoginRequest;
import com.example.sts_passenger.login.LoginResponse;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView text;
    EditText email, password;
    Button loginBtn, signup;
    TextView tvIpAddress;

    SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // ----------------- do the changes
        text = findViewById(R.id.adminText);
        email = findViewById(R.id.adminUsername);
        password = findViewById(R.id.adminPassword);
        loginBtn = findViewById(R.id.adminLoginBtn);
        signup = findViewById(R.id.signup);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        tvIpAddress = findViewById(R.id.tv_ip);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(loginRequest());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // check if user is logged and start the dashboard intent
        if (sharedPrefManager.isLogged()) {
            Intent intent = new Intent(LoginActivity.this, PassengerHomePage.class);
            // setFlags clears previous tasks
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    // to create the login request
    public LoginRequest loginRequest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email.getText().toString());
        loginRequest.setPassword(password.getText().toString());
//        loginRequest.setIpaddress(getIpAddress());
        return loginRequest;
    }

    public void login(LoginRequest loginRequest) {
        Call<LoginResponse> loginResponseCall = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().login(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (response.isSuccessful()) {
                    if (loginResponse != null && loginResponse.getStatus() == 200) {
                        sharedPrefManager.saveUser(loginResponse.getUser());
                        Toast.makeText(LoginActivity.this, "user successfully logged in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, PassengerHomePage.class);
                        // setFlags clears previous tasks
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        //                    tvIpAddress.setText(getIpAddress());
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "onFailure: " +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // extract ip address
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