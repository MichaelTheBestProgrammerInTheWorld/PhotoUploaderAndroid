package com.michaelmagdy.photouploaderandroid.model.webservice;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST("upload")
    Call<ApiResponse> uploadImage(@Part MultipartBody.Part image);

    @GET("files")
    Call<ApiResponse> getImages();
}
