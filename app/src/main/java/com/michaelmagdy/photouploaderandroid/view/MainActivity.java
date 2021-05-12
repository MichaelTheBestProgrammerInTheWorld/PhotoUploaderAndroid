package com.michaelmagdy.photouploaderandroid.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.michaelmagdy.photouploaderandroid.R;
import com.michaelmagdy.photouploaderandroid.model.Repository;
import com.michaelmagdy.photouploaderandroid.model.webservice.FileInfos;
import com.michaelmagdy.photouploaderandroid.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton uploadBtn;
    private ProgressBar progressBar;
    private TextView noImgsTxt;
    private RecyclerView recyclerView;
    private  ImagesListAdapter imagesListAdapter;
    public static final int PICK_IMAGE_REQUEST_CODE = 100;
    private MainActivityViewModel mainActivityViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        initViews();
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
            }
        });
        mainActivityViewModel.getImagesLiveData().observe(this, new Observer<List<FileInfos>>() {
            @Override
            public void onChanged(List<FileInfos> fileInfos) {
                progressBar.setVisibility(View.GONE);
                if (fileInfos.isEmpty()){
                    noImgsTxt.setVisibility(View.VISIBLE);
                } else {
                    noImgsTxt.setVisibility(View.GONE);
                    imagesListAdapter.submitList(fileInfos);
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    private void initViews() {

        uploadBtn = findViewById(R.id.upload_button);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.images_list);
        noImgsTxt = findViewById(R.id.textView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        imagesListAdapter = new ImagesListAdapter();
        recyclerView.setAdapter(imagesListAdapter);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK
                && data != null){

            Uri uri = data.getData();
            String imagePath = getPath(uri);
            if (imagePath.isEmpty()){
                Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show();
            } else {
                uploadToServer(imagePath);
            }
        }
    }

    private void uploadToServer(String imagePath) {

        progressBar.setVisibility(View.VISIBLE);
        mainActivityViewModel.uploadImage(imagePath, new Repository.UploadCallbacks() {
            @Override
            public void onSuccess(String msg) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String msg) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}