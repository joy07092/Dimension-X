package com.example.wallpaperx.Models;

import java.util.List;

public class CuratedApiResponse {   //defining all the objects for the response api parse
    public int page;
    public int per_page;
    public List<Photo> photos;      //photo is also a model cause in the json->java it has own class
    public String next_page;

    public int getPage() {
        return page;
    }      //getter method to return the value of the attribute

    public int getPer_page() {
        return per_page;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getNext_page() {
        return next_page;
    }

}
