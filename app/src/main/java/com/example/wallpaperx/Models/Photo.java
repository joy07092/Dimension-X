package com.example.wallpaperx.Models;

import java.io.Serializable;

public class Photo implements Serializable {  //cause for the implementation is we are sending particular photo object to another activity to use it later
    public int id;
    public int width;
    public int height;
    public String url;
    public String photographer;
    public String photographer_url;
    public int photographer_id;
    public String avg_color;
    public Src src;                          //Src is another model
    public boolean liked;
    public String alt;

    public int getId() {
        return id;
    }    //getter method to return the value of the attribute

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public String getPhotographer_url() {
        return photographer_url;
    }

    public int getPhotographer_id() {
        return photographer_id;
    }

    public String getAvg_color() {
        return avg_color;
    }

    public Src getSrc() {
        return src;
    }

    public boolean isLiked() {
        return liked;
    }

    public String getAlt() {
        return alt;
    }
}
