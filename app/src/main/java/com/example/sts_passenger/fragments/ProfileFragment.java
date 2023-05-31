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

import com.example.sts_passenger.Consts;
import com.example.sts_passenger.R;
import com.example.sts_passenger.activities.LoginActivity;
import com.example.sts_passenger.apiservices.Client;
import com.example.sts_passenger.apiservices.request.LogoutPassenger;
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
    Uri imageSrcUri;
    // Getting photo File
    File savedPhotoFile;




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

    /*@Override
    public void onStart() {
        super.onStart();

        String profilePhotoFilePath = sharedPrefManager.getSavedPassengerPhoto();
        if (profilePhotoFilePath != null) {
            savedPhotoFile = new File(profilePhotoFilePath);
            if (savedPhotoFile.exists()) {
                Uri imageUri = FileProvider.getUriForFile(getActivity(), "com.example.sts_passenger.fileprovider", savedPhotoFile);
                // Grant URI permission if targeting API 24 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    getActivity().grantUriPermission(getActivity().getPackageName(), imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                // set image view
                profile_picture.setImageURI(imageUri);
            } else {
                profile_picture.setImageResource(R.drawable.profile);
            }
        } else {
            profile_picture.setImageResource(R.drawable.profile);
        }
    }*/

    @Override
    public void onStart() {
        super.onStart();

        String profilePhotoFilePath = sharedPrefManager.getSavedPassengerPhoto();
        if (profilePhotoFilePath != null) {
            File savedPhotoFile = new File(profilePhotoFilePath);
            if (isImageFileValid(savedPhotoFile)) {
                Uri imageUri = Uri.fromFile(savedPhotoFile);
                profile_picture.setImageURI(imageUri);
            } else {
                profile_picture.setImageResource(R.drawable.profile);
            }
        } else {
            profile_picture.setImageResource(R.drawable.profile);
        }
    }

    public boolean isImageFileValid(File file) {
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            return options.outWidth != -1 && options.outHeight != -1;
        }
        return false;
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
        Call<ResponseBody> req = Client.getInstance(Consts.BASE_URL_PASSENGER_AUTH).getRoute().uploadProfilePhoto(photo, session.getToken(), session.getPassenger().getPassengerId());
        Log.i("TAG", "uploadImage: enqueueing " + photo + " " + session.getPassenger().getPassengerId());
        req.enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              if (response.isSuccessful()) {
                  Log.i("TAG", "onResponse: image uploaded" );
                  Log.i("TAG", "onResponse: success file name " + photo);

                  boolean success = saveFile(response.body(), file);

                  if (success) {
                      Log.i("TAG", "onResponse: Image saved successfully");

                      // Store the file path in SharedPreferences
                      sharedPrefManager.savePassengerPhoto(file);
                  }

              }
          }

         @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
             Log.i("TAG", "onFailure: " + t.getLocalizedMessage());
         }
        });
    }

    // saveFile Boolean function
    private boolean saveFile(ResponseBody responseBody, File file) {
        try {
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(responseBody.source());
            sink.close();

            return true;  // File saved successfully
        } catch (IOException e) {
            e.printStackTrace();

            return false; // Failed to save the file
        }
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