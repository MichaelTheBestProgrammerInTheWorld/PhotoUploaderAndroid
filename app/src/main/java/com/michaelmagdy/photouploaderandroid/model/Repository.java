package com.michaelmagdy.photouploaderandroid.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.michaelmagdy.photouploaderandroid.model.webservice.ApiClient;
import com.michaelmagdy.photouploaderandroid.model.webservice.ApiResponse;
import com.michaelmagdy.photouploaderandroid.model.webservice.FileInfos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private UploadCallbacks uploadCallbacks;
    private MutableLiveData<List<FileInfos>> imagesLiveData = new MutableLiveData<>();
    private List<FileInfos> imagesList = new ArrayList<>();

    public void uploadImageRequest(String imagePath, UploadCallbacks uploadCallbacks) {

        this.uploadCallbacks = uploadCallbacks;
        File imageFile = new File(imagePath);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part formData =
                MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);

        ApiClient.getApiClient().getApiInterface()
                .uploadImage(formData)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        ApiResponse result = response.body();
                        if (result != null) {
                            int success = result.getSuccess();
                            String message = result.getMessage();


                            if (success == 1) {

                                uploadCallbacks.onSuccess(message);

                            } else {

                                uploadCallbacks.onFail(message);
                            }
                        } else {

                            uploadCallbacks.onFail("zero");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                        uploadCallbacks.onFail(t.getMessage());
                    }
                });
    }


    public interface UploadCallbacks {

        void onSuccess(String msg);

        void onFail(String msg);
    }


    public void getImagesRequest() {

        imagesList.clear();
        ApiClient.getApiClient().getApiInterface()
                .getImages()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        ApiResponse result = response.body();
                        if (result != null) {
                            int success = result.getSuccess();

                            if (success == 1) {

                                imagesList.addAll(result.getFileInfos());

                                if (imagesList != null) {
                                    imagesLiveData.setValue(imagesList);
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                        Log.d("network_fail", t.getMessage());
                    }
                });
    }

    public MutableLiveData<List<FileInfos>> getImagesLiveData() {
        return imagesLiveData;
    }
}
