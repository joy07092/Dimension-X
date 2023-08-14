package com.example.wallpaperx.Listeners;

import com.example.wallpaperx.Models.CuratedApiResponse;

public interface CuratedResponseListener {

    void onFetch(CuratedApiResponse response, String message);
    void onError(String message);
}
