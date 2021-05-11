package com.michaelmagdy.photouploaderandroid.model.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileInfos {
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("name")
    private String name;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
