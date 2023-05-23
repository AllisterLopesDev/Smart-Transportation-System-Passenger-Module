package com.example.sts_passenger.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.activities.LoginActivity;
import com.example.sts_passenger.R;
import com.example.sts_passenger.apiservices.request.LogoutPassenger;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.sharedpref.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {



    /* image upload */
    int SELECT_IMAGE_CODE=1;
    FloatingActionButton addImageBtn;
    CircleImageView profile_picture;
    Uri imageSrcUri;




    Button logout,tripHistory;
    SharedPrefManager sharedPrefManager;
    TextView tv_title,name_text,email_text,contact_text,address_text;
    CardView cardViewProfile;


    private Context mContext;
    private Session session;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSharedPrefManager();
        initView(view); // views initialization


        /* upload image btn */
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"title"),SELECT_IMAGE_CODE);
            }
        });



        tripHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripHistory fragment = new TripHistory();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_profile_container, fragment);
                transaction.commit();

                // hide views
                hideViewsOnFragTransaction();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(logoutRequest());
            }
        });
    }

    private void hideViewsOnFragTransaction() {
        tripHistory.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
        cardViewProfile.setVisibility(View.GONE);
        tv_title.setVisibility(View.GONE);

    }

    //------------------------------logout-------------------------
    public LogoutPassenger logoutRequest(){
        LogoutPassenger logoutRequest=new LogoutPassenger();
        logoutRequest.setToken(getSessionToken());
        return logoutRequest;
    }
    public void logout(LogoutPassenger logoutRequest){
        Call<com.example.sts_passenger.apiservices.response.LogoutPassenger> logoutResponseCall= Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().logout(logoutRequest);
        logoutResponseCall.enqueue(new Callback<com.example.sts_passenger.apiservices.response.LogoutPassenger>() {
            @Override
            public void onResponse(Call<com.example.sts_passenger.apiservices.response.LogoutPassenger> call, Response<com.example.sts_passenger.apiservices.response.LogoutPassenger> response) {
                com.example.sts_passenger.apiservices.response.LogoutPassenger logoutResponse=response.body();
                if (response.isSuccessful()){
//                    Toast.makeText(AdminDashboard.this, "Logout successful", Toast.LENGTH_SHORT).show();
                    if(logoutResponse != null && logoutResponse.getStatus() == 200){
                        sharedPrefManager.logout();
                        Toast.makeText(mContext, "Logout successful", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(mContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
//                        finish();
                    }
                } else {
                    Toast.makeText(getActivity(), "onResponse: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.example.sts_passenger.apiservices.response.LogoutPassenger> call, Throwable t) {
                Toast.makeText(mContext, "onFailure: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /* Custom functions required for this activity */

    // get current user token from shared pref manager
    public String getSessionToken() {
        sharedPrefManager = new SharedPrefManager(mContext);
        return sharedPrefManager.getUser().getToken();
    }

    public void initView(View view){
        logout = view.findViewById(R.id.logout);
        tripHistory = view.findViewById(R.id.tripHistory);
        cardViewProfile = view.findViewById(R.id.cardView_profile);
        tv_title = view.findViewById(R.id.sts_title);
        name_text = view.findViewById(R.id.name_text);
        email_text = view.findViewById(R.id.email_text);
        contact_text = view.findViewById(R.id.contact_text);
        address_text = view.findViewById(R.id.address_text);
        profile_picture = view.findViewById(R.id.profile_picture);
        addImageBtn = view.findViewById(R.id.upload_img);

        String name = session.getPassenger().getFirstname()+ " "+ session.getPassenger().getLastname();
        name_text.setText(name);
        email_text.setText(session.getUser().getEmail());
        contact_text.setText(session.getPassenger().getContact());
        address_text.setText(session.getPassenger().getAddress());
    }

    // SharedPrefManager function
    public void setSharedPrefManager(){
        sharedPrefManager = new SharedPrefManager(requireContext());
        session = sharedPrefManager.getSavedSessionOnLogin();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1){
            imageSrcUri = data.getData();
            profile_picture.setImageURI(imageSrcUri);
            Log.i("TAG", "onActivityResult: uploaded image url "+imageSrcUri);
            uplaodImage(imageSrcUri);
        }
    }



/* upload image function */
    public void uplaodImage(Uri imageSrcUri){
        File file = new File(String.valueOf(imageSrcUri));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        retrofit2.Call<okhttp3.ResponseBody> req = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              Log.i("TAG", "onResponse: image uploaded" );
          }

         @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {

         }
        });
    }



}