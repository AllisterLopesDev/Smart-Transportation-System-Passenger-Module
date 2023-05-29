package com.example.sts_passenger.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.sts_passenger.R;
import com.example.sts_passenger.assets.QRCodeGenerator;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

public class PassQrCode extends AppCompatActivity {
    ImageView imageView;
    SharedPrefManager sharedPrefManager;
    String data= "";
    String passengerId = "";
    String passid = "";
    private Session savedSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_qr_code);

        setSharedPrefManager();
        Intent i = getIntent();
        Integer passId = i.getIntExtra("passId",0);
        passid = String.valueOf(passId);
        passengerId = savedSession.getPassenger().getPassengerId().toString();
        data = passid+" | "+passengerId;

        imageView = findViewById(R.id.imgView);
        QRCodeGenerator.generateQRCode(data, imageView);
    }

    public void setSharedPrefManager() {
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        savedSession = sharedPrefManager.getSavedSessionOnLogin();
    }
}