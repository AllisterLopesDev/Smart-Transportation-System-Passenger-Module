package com.example.sts_passenger.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sts_passenger.R;

public class ScheduleInfo extends AppCompatActivity {

    TextView busNo,busType,seatCount,arrStand,deptStand,arrTime,deptTime,duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_info);
        busNo = findViewById(R.id.busNo);
        busType = findViewById(R.id.busType);
        seatCount = findViewById(R.id.seatAvai);
        arrStand = findViewById(R.id.arrivalStand);
        deptStand = findViewById(R.id.deptStand);
        arrTime = findViewById(R.id.arrivalTime);
        deptTime = findViewById(R.id.deptTime);
        duration = findViewById(R.id.duration);

        Intent i = getIntent();
        String busNo1 = i.getStringExtra("busNo");
        String busType1 = i.getStringExtra("busType");
        String seatCount1 = String.valueOf(i.getIntExtra("seatCount",0));
        String arrStand1 = i.getStringExtra("arrStand");
        String deptStand1 = i.getStringExtra("deptStand");
        String arrTime1 = i.getStringExtra("arrTime");
        String deptTime1 = i.getStringExtra("deptTime");
        String duration1 = i.getStringExtra("duration");
//        busNo,busType,seatCount,arrStand,deptStand,arrTime,deptTime,duration

        busNo.setText("bus Regno : "+busNo1);
        busType.setText("bus type : "+busType1);
        seatCount.setText("Available Seats : "+seatCount1);
        arrStand.setText("Arrival stand : "+arrStand1);
        deptStand.setText("Departure Stand : "+deptStand1);
        arrTime.setText("Arrival at : "+arrTime1);
        deptTime.setText("Departure at : "+deptTime1);
        duration.setText("Duration : "+duration1);

    }
}