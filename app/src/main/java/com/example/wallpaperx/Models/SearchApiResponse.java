package com.example.wallpaperx.Models;

import java.util.List;

public class SearchApiResponse {   //same as curatedapiresponse except
    public int page;
    public int total_results;     //this is extra to get the total result for the search we made
    public int per_page;
    public List<Photo> photos;
    public String next_page;

    public int getPage() {
        return page;
    }

    public int getPer_page() {
        return per_page;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getNext_page() {
        return next_page;
    }

    public int getTotal_results() {
        return total_results;
    }
}
