package com.example.wallpaperx.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaperx.Listeners.OnRecyclerClickListener;
import com.example.wallpaperx.Models.Photo;
import com.example.wallpaperx.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CuratedAdapter extends  RecyclerView.Adapter<CuratedViewHolder>{  //taking the viewholder to make the adapter

    Context context;  //handle to the system app currently running in
    List<Photo> list;  //to show the pictures and have to select the <Photo> of my package name
    OnRecyclerClickListener listener;  //onclicklistener for recycleview in Listeners

    public CuratedAdapter(Context context, List<Photo> list, OnRecyclerClickListener listener) {  //constructor
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CuratedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CuratedViewHolder(LayoutInflater.from(context).inflate(R.layout.home_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CuratedViewHolder holder, int position) {  //show the images in home_list
        Picasso.get().load(list.get(position).getSrc().getMedium()).placeholder(R.drawable.placeholder).into(holder.imageView_list); //using picasso library to load the images and only getUrl() won't load the images so we use getSrc().getMedium() given in Json
        holder.home_list_container.setOnClickListener(new View.OnClickListener() { //onClicklistener for the cardview
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    } //Returns the total number of items in the data set held by the adapter.
}


class CuratedViewHolder extends RecyclerView.ViewHolder {
    CardView home_list_container;    //cardview container and imageview from home_list are the objects
    ImageView imageView_list;

    public CuratedViewHolder(@NonNull View itemView) {
        super(itemView);
        home_list_container = itemView.findViewById(R.id.home_list_container);   //initializing the objects
        imageView_list = itemView.findViewById(R.id.imageView_list);
    }
}