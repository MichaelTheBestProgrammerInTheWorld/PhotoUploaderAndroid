package com.michaelmagdy.photouploaderandroid.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.michaelmagdy.photouploaderandroid.R;
import com.michaelmagdy.photouploaderandroid.model.webservice.FileInfos;
import com.squareup.picasso.Picasso;


public class ImagesListAdapter extends ListAdapter<FileInfos, ImagesListAdapter.ImagesViewHolder> {


    protected ImagesListAdapter() {
        super(diffCallback);
    }

    public static final DiffUtil.ItemCallback<FileInfos> diffCallback =
            new DiffUtil.ItemCallback<FileInfos>() {
                @Override
                public boolean areItemsTheSame(@NonNull FileInfos oldItem, @NonNull FileInfos newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull FileInfos oldItem, @NonNull FileInfos newItem) {
                    return oldItem.getUrl().equals(newItem.getUrl());
                }
            };

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ImagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {

        Picasso.get()
                .load("http://10.0.2.2:8080/files/" + getItem(position).getName())
                .placeholder(android.R.drawable.stat_sys_download)
                .into(holder.imageView);
    }

    class ImagesViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_list_item);
        }
    }
}
