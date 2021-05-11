package com.michaelmagdy.photouploaderandroid.model.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {


    @Expose
    @SerializedName("fileInfos")
    private List<FileInfos> fileInfos;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("success")
    private int success;

    public List<FileInfos> getFileInfos() {
        return fileInfos;
    }

    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }
}
