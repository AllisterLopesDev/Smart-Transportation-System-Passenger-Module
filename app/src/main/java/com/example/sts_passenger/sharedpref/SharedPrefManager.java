package com.example.sts_passenger.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.model.Halts;
import com.example.sts_passenger.model.Passenger;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.model.User;


public class SharedPrefManager {
    SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        this.context = context;
    }



    public void saveUser(User user) {
        sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF_AUTH, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("id", user.getUserId());
        editor.putString("email", user.getEmail());
        editor.putString("token", user.getToken());
        editor.putBoolean("logged", true);
        editor.apply();
    }

    public User getUser() {
        sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF_AUTH, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("token", null));
    }

    public void savePassengerData(Passenger passenger){
        sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF_AUTH, Context.MODE_PRIVATE);
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
        sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF_AUTH, Context.MODE_PRIVATE);
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
        sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF_AUTH, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged", false);
    }



    public void logout() {
        sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF_AUTH, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public void savePassSource(Halts halts) {
        sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF_PASS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("id", halts.getId());
        editor.putString("name", halts.getName());
        editor.apply();
    }

    public void savePassengerOnLogin(Session session) {
        sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF_AUTH, Context.MODE_PRIVATE);

        // editor to insert data
        editor = sharedPreferences.edit();

        // put data
        editor.putInt("userId", session.getUser().getUserId());
        editor.putString("email", session.getUser().getEmail());
        editor.putString("token", session.getToken());
        editor.putInt("passengerId", session.getPassenger().getPassengerId());
        editor.putString("firstName", session.getPassenger().getFirstname());
        editor.putString("lastName", session.getPassenger().getLastname());
        editor.putString("contact", session.getPassenger().getContact());
        editor.putString("address", session.getPassenger().getAddress());
        editor.putString("gender", session.getPassenger().getGender());
        editor.putString("category", session.getPassenger().getCategory());
        editor.putString("dob", session.getPassenger().getDob());
        editor.putBoolean("logged", true);
        editor.apply();
    }

    public Session getSavedSessionOnLogin() {
        sharedPreferences = context.getSharedPreferences(Consts.SHARED_PREF_AUTH, Context.MODE_PRIVATE);

        // User
        int userId = sharedPreferences.getInt("userId", -1);
        String email = sharedPreferences.getString("email", "");

        // Session
        String token = sharedPreferences.getString("token", "");

        // Passenger
        int passengerId = sharedPreferences.getInt("passengerId", -1);
        String firstname = sharedPreferences.getString("firstName", "");
        String lastname = sharedPreferences.getString("lastName", "");
        String contact = sharedPreferences.getString("contact", "");
        String address = sharedPreferences.getString("address", "");
        String gender = sharedPreferences.getString("gender", "");
        String category = sharedPreferences.getString("category", "");
        String dateOfBirth = sharedPreferences.getString("dob", "");

        // Create a new user
        User user = new User(userId, email);
        // Create new passenger
        Passenger passenger = new Passenger(firstname, lastname, contact, address, dateOfBirth, category, gender, passengerId);
        // pass Session in return with user, passenger and token in constructor
        return new Session(user, passenger, token);
    }

/*    public User getPassSource() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_PASS_DATA, Context.MODE_PRIVATE);
    }*/

}