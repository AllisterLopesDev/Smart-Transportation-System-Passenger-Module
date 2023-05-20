package com.example.sts_passenger.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sts_passenger.apiservices.response.RegisterUser;
import com.example.sts_passenger.model.Halts;
import com.example.sts_passenger.model.Passenger;
import com.example.sts_passenger.model.User;


public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "stsadmin";
    private static final String SHARED_PREF_PASS_DATA = "stspass";
    SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        this.context = context;
    }



    public void saveUser(User user) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("id", user.getUserId());
        editor.putString("email", user.getEmail());
        editor.putString("token", user.getToken());
        editor.putBoolean("logged", true);
        editor.apply();
    }

    public User getUser() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("token", null));
    }

    public void addUserDetails(Passenger passenger){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("firstname",passenger.getFirstname());
        editor.putString("lastname",passenger.getLastname());
        editor.putString("contact",passenger.getContact());
        editor.putString("address",passenger.getAddress());
        editor.putString("gender",passenger.getGender());
        editor.putString("category",passenger.getCategory());
        editor.putString("dob",passenger.getDob());
        editor.putInt("id",passenger.getPassengerId());
        editor.apply();
    }

    public Passenger getPassenger() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Passenger(sharedPreferences.getString("firstname", null),
                sharedPreferences.getString("lastname", null),
                sharedPreferences.getString("contact", null),
                sharedPreferences.getString("address", null),
                sharedPreferences.getString("gender", null),
                sharedPreferences.getString("category", null),
                sharedPreferences.getString("dob", null),
                sharedPreferences.getInt("id", -1));
    }

    public boolean isLogged() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged", false);
    }



    public void logout() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public void savePassSource(Halts halts) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_PASS_DATA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("id", halts.getId());
        editor.putString("name", halts.getName());
        editor.apply();
    }

/*    public User getPassSource() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_PASS_DATA, Context.MODE_PRIVATE);
    }*/

}