package com.example.sts_passenger.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sts_passenger.model.User;


public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "stsadmin";
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

    public void addUserDetails(User user){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString("firstname",user.getFirstname());
        editor.putString("lastname",user.getLastname());
        editor.putString("contact",user.getContact());
        editor.putString("address",user.getAddress());
        editor.putString("gender",user.getGender());
        editor.putString("category",user.getCategory());
        editor.putString("dob",user.getDob());
        editor.apply();
    }

    public boolean isLogged() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged", false);
    }

    public User getUser() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("token", null));
    }

    public void logout() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}