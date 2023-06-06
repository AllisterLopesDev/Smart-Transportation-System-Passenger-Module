package com.example.sts_passenger.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.activities.LoginActivity;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.request.LogoutPassenger;
import com.example.sts_passenger.apiservices.response.PhotoUploadResponse;
import com.example.sts_passenger.model.Passenger;
import com.example.sts_passenger.model.Session;
import com.example.sts_passenger.sharedpref.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {



    /* image upload */
    int SELECT_IMAGE_CODE=1;
    FloatingActionButton addImageBtn;
    CircleImageView profile_picture;
    // Getting photo File
    File savedPhotoFile;




    Button logout,tripHistory;
    SharedPrefManager sharedPrefManager;
    TextView tv_title,name_text,email_text,contact_text,address_text,gender_text,category_text;
    CardView cardViewProfile;


    private Context mContext;
    LinearLayout layout3;
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

        // Get passenger photo on Login
        loadPassengerPhoto();


        /* upload image btn */
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check for permission
                if (checkPermission()) {
                    mGetProfileImg.launch("image/*");
                } else {
                    requestPermission();
                }
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

    @Override
    public void onStart() {
        super.onStart();

        loadPassengerPhoto();
    }

    /* load passenger photo function */
    private void loadPassengerPhoto() {
        if (sharedPrefManager.getPassengerPhotoFileName() != null) {
            Log.i("TAG", "loadPassengerPhoto: photo fileName is not null");
            Log.i("TAG", "loadPassengerPhoto: SPM.gPPFN" + sharedPrefManager.getPassengerPhotoFileName());
            Log.i("TAG", "loadPassengerPhoto: SPM.savedSession" + sharedPrefManager.getSavedSessionOnLogin().getPassenger().getFileName());
            // SharedPref passenger photo filename
            String fileName = sharedPrefManager.getPassengerPhotoFileName();
            // Load the image using Glide
            // Construct photo URL
            String photoUrl = Consts.BASE_URL_PASSENGER_AUTH + Consts.ENDPOINT_GET_PROFILE_PIC + fileName;
            // Add the fileName to the url at the end
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.profile);

            Glide.with(requireContext())
                    .load(photoUrl)
                    .apply(requestOptions)
                    .into(profile_picture);
        }
    }

    private void hideViewsOnFragTransaction() {
        tripHistory.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
//        cardViewProfile.setVisibility(View.GONE);
//        tv_title.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);

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
//        cardViewProfile = view.findViewById(R.id.cardView_profile);
//        tv_title = view.findViewById(R.id.sts_title);
        name_text = view.findViewById(R.id.name_text);
        email_text = view.findViewById(R.id.email_text);
        contact_text = view.findViewById(R.id.contact_text);
        address_text = view.findViewById(R.id.address_text);
        gender_text = view.findViewById(R.id.genger_text);
        category_text = view.findViewById(R.id.category_text);
        layout3 = view.findViewById(R.id.linearLayout3);

        profile_picture = view.findViewById(R.id.profile_picture);
        addImageBtn = view.findViewById(R.id.upload_img);

        String name = session.getPassenger().getFirstname()+ " "+ session.getPassenger().getLastname();
        name_text.setText(name);
        email_text.setText(session.getUser().getEmail());
        contact_text.setText(session.getPassenger().getContact());
        address_text.setText(session.getPassenger().getAddress());
        gender_text.setText(session.getPassenger().getGender());
        category_text.setText(session.getPassenger().getCategory());
    }

    // SharedPrefManager function
    public void setSharedPrefManager(){
        sharedPrefManager = new SharedPrefManager(requireContext());
        session = sharedPrefManager.getSavedSessionOnLogin();
    }


    /*
    *   handle Uri mGetProfileImage
    *   replacing onActivityResult() as it is deprecated
    *
    *   call the mGetProfileImg in the onCreate method
    * */
    ActivityResultLauncher<String> mGetProfileImg = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            String imagePath = getPathFromUri(result);

            if (imagePath != null) {
                // set image to view
                profile_picture.setImageURI(result);

                Log.i("TAG", "Selected image path: " + imagePath);
                File photo = new File(imagePath);
                uploadImage(photo);
            } else {
                Log.e("TAG", "Failed to get the file path from the URI");
            }
        }
    });




    // Required to get the image path to upload the image
    private String getPathFromUri(Uri uri) {
        String path = null;

        if (uri != null) {
            try {
                ContentResolver contentResolver = requireContext().getContentResolver();
                String fileName = getFileName(uri);

                if (fileName != null) {
                    File cacheDir = requireContext().getCacheDir();
                    File tempFile = new File(cacheDir, fileName);

                    InputStream inputStream = contentResolver.openInputStream(uri);
                    FileOutputStream outputStream = new FileOutputStream(tempFile);
                    copyFile(inputStream, outputStream);
                    path = tempFile.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path;
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        Cursor cursor = null;

        try {
            cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (displayNameIndex != -1) {
                    fileName = cursor.getString(displayNameIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return fileName;
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4 * 1024]; // 4KB buffer
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }




    /* upload image function */
    public void uploadImage(File file){
        // Create a request body with File(file)
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        // Create a MultipartBody.Part from the request body
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestBody);
        Log.i("TAG", "uploadImage: file name " + file.getName());

        // Api call
        Call<PhotoUploadResponse> req = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().uploadProfilePhoto(photo, session.getToken(), session.getPassenger().getPassengerId());
        Log.i("TAG", "uploadImage: enqueueing " + photo + " " + session.getPassenger().getPassengerId());
        req.enqueue(new Callback<PhotoUploadResponse>() {
          @Override
          public void onResponse(Call<PhotoUploadResponse> call, Response<PhotoUploadResponse> response) {
              if (response.isSuccessful()) {
                  Log.i("TAG", "onResponse: image uploaded" );
                  Log.i("TAG", "onResponse: success file name " + photo);
                  if (response.body() != null && response.body().getStatusCode() == 200) {
                      PhotoUploadResponse uploadResponse = response.body();
                      Passenger passenger = new Passenger();
                      passenger.setFileName(uploadResponse.getFileName());
                      sharedPrefManager.savePassengerPhotoFileName(passenger);

                      // Load passenger photo after uploading
                      loadPassengerPhoto();
                  }
              }
          }

         @Override
          public void onFailure(Call<PhotoUploadResponse> call, Throwable t) {
             Log.i("TAG", "onFailure: " + t.getLocalizedMessage());
         }
        });
    }


    // Check if permission is granted for read external storage
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    // Permission request
    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Consts.PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Consts.PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                mGetProfileImg.launch("image/*");
            } else {
                // permission denied
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "onRequestPermissionsResult: Permission Denied " + requestCode);
            }
        }
    }
}