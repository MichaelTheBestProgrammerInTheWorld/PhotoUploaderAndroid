package com.michaelmagdy.photouploaderandroid.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.michaelmagdy.photouploaderandroid.model.Repository;
import com.michaelmagdy.photouploaderandroid.model.webservice.FileInfos;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private Repository repository;
    private MutableLiveData<List<FileInfos>> imagesLiveData = new MutableLiveData<>();

    public MainActivityViewModel() {
        repository = new Repository();
        repository.getImagesRequest();
    }

    public void uploadImage(String imagePath, Repository.UploadCallbacks uploadCallbacks){

        repository.uploadImageRequest(imagePath, uploadCallbacks);
    }

    public MutableLiveData<List<FileInfos>> getImagesLiveData() {

        this.imagesLiveData = repository.getImagesLiveData();
        return imagesLiveData;
    }
}
