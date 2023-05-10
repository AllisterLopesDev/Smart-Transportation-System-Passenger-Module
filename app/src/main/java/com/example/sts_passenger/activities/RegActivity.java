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
import com.example.sts_passenger.send_otp.RequestOtp;
import com.example.sts_passenger.send_otp.ResponseOtp;

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
//                register(createRequest());
                sendOtp(createOtpRequest());
            }
        });

    }

    //-------------------------------Send otp --------------------------------//

    public RequestOtp createOtpRequest() {
        RequestOtp requestOtp = new RequestOtp();
        requestOtp.setEmail(getEmail());
        return requestOtp;
    }

    public void sendOtp(RequestOtp requestOtp) {
        Call<ResponseOtp> responseOtpCall = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().sendOtp(requestOtp);
        responseOtpCall.enqueue(new Callback<ResponseOtp>() {
            @Override
            public void onResponse(Call<ResponseOtp> call, Response<ResponseOtp> response) {

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
            public void onFailure(Call<ResponseOtp> call, Throwable t) {
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

}