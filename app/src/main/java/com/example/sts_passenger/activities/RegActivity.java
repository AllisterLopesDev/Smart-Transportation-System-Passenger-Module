package com.example.sts_passenger.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.response.RegistrationOtp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegActivity extends AppCompatActivity {


    EditText email, password;
    Button regBtn;
    TextView tv_ip;

    String userEmail,userPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        regBtn = findViewById(R.id.reg);
        tv_ip = findViewById(R.id.tv_ip);

        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();

                if (Email.isEmpty()) {
                    email.setError("Email is required");
                } else if (!isValidEmail(Email)) {
                    email.setError("Invalid email address");
                } else if (Password.isEmpty()){
                    password.setError("Input is required");
                }else {
                    sendOtp(createOtpRequest());
                }



            }
        });

    }

    //-------------------------------Send otp --------------------------------//

    public com.example.sts_passenger.apiservices.request.RegistrationOtp createOtpRequest() {
        com.example.sts_passenger.apiservices.request.RegistrationOtp requestOtp = new com.example.sts_passenger.apiservices.request.RegistrationOtp();
        requestOtp.setEmail(getEmail());
        return requestOtp;
    }

    public void sendOtp(com.example.sts_passenger.apiservices.request.RegistrationOtp requestOtp) {
        Call<RegistrationOtp> responseOtpCall = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().sendOtp(requestOtp);
        responseOtpCall.enqueue(new Callback<RegistrationOtp>() {
            @Override
            public void onResponse(Call<RegistrationOtp> call, Response<RegistrationOtp> response) {

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegActivity.this, "OTP send ..." + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        tv_ip.setText(response.body().getMessage());

                        Intent i = new Intent(getApplicationContext(), VerifyOtp.class);
                        i.putExtra("email", getEmail());
                        i.putExtra("password",getPassword());
                        startActivity(i);
                    } else {
                        Toast.makeText(RegActivity.this, "request failed"+ response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        tv_ip.setText(response.body().getMessage());
                    }
                }
            }



            @Override
            public void onFailure(Call<RegistrationOtp> call, Throwable t) {
                Toast.makeText(RegActivity.this, "failed " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getEmail() {
        return email.getText().toString();
    }

    private String getPassword() {
        return password.getText().toString();
    }



    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

}