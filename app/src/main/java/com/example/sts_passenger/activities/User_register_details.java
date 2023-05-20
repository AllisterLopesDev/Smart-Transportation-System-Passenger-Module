package com.example.sts_passenger.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.request.RegisterPassengerRequest;
import com.example.sts_passenger.apiservices.response.RegisterPassenger;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.User;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_register_details extends AppCompatActivity {

    EditText fname, lname,address,contact,tv_dob;
    Button buttonRegister;
    Spinner spinner_gender,spinner_category;
    private int year, month, day;
    String genderData,categoryData,dobData;
    CalendarDate calendarDays;
    TextView tvShowServerMessage;
    SharedPrefManager sharedPrefManager;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reg_details);

        fname = findViewById(R.id.firstName);
        lname = findViewById(R.id.lastName);
        address = findViewById(R.id.address);
        spinner_category = findViewById(R.id.category);
        contact = findViewById(R.id.contact);
        spinner_gender = findViewById(R.id.gender);
        tv_dob = findViewById(R.id.dob);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendarDays = new CalendarDate();


        buttonRegister = findViewById(R.id.adddetailsbtn);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        // ############### gender spinner call #############
        gender();
        // ############### Date of birth spinner call #############
//        tv_dob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getCurrentDate();
//                Log.i("TAG", "onClick: tv_date ");
//            }
//        });
        // ############### category spinner call #############
        category();

        // ###################### submit button ########################
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserInfo(createRequest());
            }
        });
    }

    public RegisterPassengerRequest createRequest() {
        RegisterPassengerRequest userRequest = new RegisterPassengerRequest();
        userRequest.setFirstname(fname.getText().toString());
        userRequest.setLastname(lname.getText().toString());
        userRequest.setAddress(address.getText().toString());
        userRequest.setGender(getGenderData());
        userRequest.setContact(contact.getText().toString());
        userRequest.setCategory(getCategoryData());
        userRequest.setDob(tv_dob.getText().toString());
        userRequest.setUserid(sharedPrefManager.getUser().getUserId());
        return userRequest;
    }

    public void addUserInfo(RegisterPassengerRequest userRequest) {
        Call<RegisterPassenger> userResponseCall = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().addDetails(sharedPrefManager.getUser().getToken(),userRequest);
        Log.i("TAG", "addUserInfo: token "+sharedPrefManager.getUser().getToken());
        userResponseCall.enqueue(new Callback<RegisterPassenger>() {
            @Override
            public void onResponse(Call<RegisterPassenger> call, Response<RegisterPassenger> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getStatus() == 200) {
                        sharedPrefManager.savePassengerData(response.body().getPassenger());
                        Toast.makeText(User_register_details.this, "user registered " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        tvShowServerMessage.setText(response.body().getMessage());
                        Intent i = new Intent(getApplicationContext(), PassengerHomePage.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(User_register_details.this, "request failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call< RegisterPassenger > call, Throwable t) {
                Toast.makeText(User_register_details.this, "failed " +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    // gender data
    public void gender(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                setGenderData(adapterView.getItemAtPosition(position).toString());
                Log.i("TAG", "onItemSelected: selected gender : "+getGenderData());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when no item is selected
            }
        });
    }

    //category data
    public void category(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                setCategoryData(adapterView.getItemAtPosition(position).toString());
                Log.i("TAG", "onItemSelected: selected gender : "+getCategoryData());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when no item is selected
            }
        });
    }

    // --------------------------- DATE OF BIRTH ------------------------

    public void getCurrentDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getApplicationContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setDobData(year + "-" + (month + 1) + "-" + dayOfMonth);
                        tv_dob.setText(getDobData());
                        Log.i("TAG", "onDateSet: "+genderData);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }


    // --------------------------- getter & setter ------------------------


    public String getGenderData() {
        return genderData;
    }

    public void setGenderData(String genderData) {
        this.genderData = genderData;
    }

    public String getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(String categoryData) {
        this.categoryData = categoryData;
    }

    public String getDobData() {
        return dobData;
    }

    public void setDobData(String dobData) {
        this.dobData = dobData;
    }
}