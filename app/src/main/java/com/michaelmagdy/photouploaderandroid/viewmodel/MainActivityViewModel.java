package com.michaelmagdy.photouploaderandroid.viewmodel;

import androidx.lifecycle.ViewModel;

import com.michaelmagdy.photouploaderandroid.model.Repository;

public class MainActivityViewModel extends ViewModel {

    private Repository repository;

    public MainActivityViewModel() {
        repository = new Repository();
    }

    public void uploadImage(String imagePath, Repository.UploadCallbacks uploadCallbacks){

        repository.uploadImageRequest(imagePath, uploadCallbacks);
    }
}
