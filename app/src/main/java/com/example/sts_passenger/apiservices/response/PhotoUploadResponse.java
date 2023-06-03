package com.example.sts_passenger.apiservices.response;

import com.google.gson.annotations.SerializedName;

public class PhotoUploadResponse {
    /*
    * {
    "file-name": "4ac87981-ef14-4f0c-bb67-e1eab5573bee-20230603163952DSC_0036.png",
    "message": "Photo uploaded successfully",
    "photo-url": "http://3.110.42.226/user/file/pic/4ac87981-ef14-4f0c-bb67-e1eab5573bee-20230603163952DSC_0036.png",
    "status": 200,
    "success": true
}
    * */

    @SerializedName("file-name")
    private String fileName;
    @SerializedName("message")
    private String message;
    @SerializedName("photo-url")
    private String photoUrl;
    @SerializedName("status")
    private int statusCode;
    @SerializedName("success")
    private boolean success;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
