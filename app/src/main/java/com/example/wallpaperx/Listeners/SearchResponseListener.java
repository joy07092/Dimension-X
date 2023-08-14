package com.example.wallpaperx.Listeners;

import com.example.wallpaperx.Models.SearchApiResponse;

public interface SearchResponseListener {

    void onFetch(SearchApiResponse response, String message);
    void onError(String message);
}
