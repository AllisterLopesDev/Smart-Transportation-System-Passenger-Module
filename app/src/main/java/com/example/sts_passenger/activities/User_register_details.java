package com.example.sts_passenger.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.request.RegisterPassengerRequest;
import com.example.sts_passenger.apiservices.response.RegisterPassenger;
import com.example.sts_passenger.model.CalendarDate;
import com.example.sts_passenger.model.Passenger;
import com.example.sts_passenger.model.User;
import com.example.sts_passenger.sharedpref.SharedPrefManager;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_register_details extends AppCompatActivity {

    EditText fname, lname,address,contact;
    TextView etDateOfBirth;
    AppCompatButton buttonRegister;
    ImageView imgDatePicker;
    Spinner spinner_gender, spinner_category;
    private int year, month, day;
    String genderData,categoryData,dobData;
    CalendarDate calendarDays;
    Passenger passenger;
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
        etDateOfBirth = findViewById(R.id.et_dob);

        /*
        * DatePicker for date of birth
        * */
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendarDays = new CalendarDate();

        // DatePicker button
        imgDatePicker = findViewById(R.id.img_datePicker);
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentDate();
            }
        });


        buttonRegister = findViewById(R.id.adddetailsbtn);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        // passenger records for registration
        passenger = new Passenger();

        // ############### gender spinner call #############
        gender();
        // ############### category spinner call #############
        category();

        // ###################### submit button ########################
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Fname = fname.getText().toString().trim();
                String Lname = lname.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String Contact = contact.getText().toString().trim();
                String Dob = etDateOfBirth.getText().toString().trim();


                if (Fname.isEmpty()) {
                    fname.setError("First name Required");
                } else if (Fname.length() < 3) {
                    fname.setError("Minimum 3 characters required");
                } else if (!isValidString(Fname)) {
                    fname.setError("Invalid input");
                } else if (Lname.isEmpty()) {
                    lname.setError("Last name Required");
                } else if (Lname.length() < 3) {
                    lname.setError("Minimum 3 characters required");
                } else if (!isValidString(Lname)) {
                    lname.setError("Invalid input");
                } else if (Contact.isEmpty()) {
                    contact.setError("Please enter contact no.");
                } else if (Contact.length() != 10) {
                    contact.setError("10 characters required");
                } else if (Address.isEmpty()) {
                    address.setError("please enter the address");
                }else if (Dob.isEmpty()) {
                    etDateOfBirth.setError("Select Dob");
                }else if (spinner_gender.getSelectedItemPosition() == 0) {
                    TextView errorText = (TextView) spinner_gender.getSelectedView();
                    errorText.setError("SELECT GENDER");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Please select a gender");
                }else if (spinner_category.getSelectedItemPosition() == 0) {
                    TextView errorText = (TextView) spinner_category.getSelectedView();
                    errorText.setError("SELECT Category");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Please select a category");
                }else{
                    addUserInfo(createRequest());
                }


            }
        });
    }

    public RegisterPassengerRequest createRequest() {
        RegisterPassengerRequest userRequest = new RegisterPassengerRequest();
        userRequest.setFirstname(fname.getText().toString());
        userRequest.setLastname(lname.getText().toString());
        userRequest.setAddress(address.getText().toString());

        userRequest.setGender(passenger.getGender());
        Log.i("TAG", "createRequest: gender "+passenger.getGender());
        userRequest.setContact(contact.getText().toString());
        userRequest.setCategory(passenger.getCategory());
        Log.i("TAG", "createRequest: gender "+passenger.getCategory());


        userRequest.setDob(etDateOfBirth.getText().toString());

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
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
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
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.gender, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapterGender);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                passenger.setGender(adapterView.getItemAtPosition(position).toString());
                Log.i("TAG", "onItemSelected: selected gender : "+passenger.getGender());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when no item is selected
            }
        });
    }

    //category data
    public void category(){
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapterCategory);

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                passenger.setCategory(adapterView.getItemAtPosition(position).toString());
                Log.i("TAG", "onItemSelected: selected gender : "+passenger.getCategory());

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
                User_register_details.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        passenger.setDob(year + "-" + (month + 1) + "-" + dayOfMonth);
                        etDateOfBirth.setText(passenger.getDob());

                        Log.i("TAG", "onDateSet: "+genderData);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }


    // Function to check if the string contains only alphabetic characters
    private boolean isValidString(String input) {
        String regex = "^[a-zA-Z]+$";
        return input.matches(regex);
    }
}