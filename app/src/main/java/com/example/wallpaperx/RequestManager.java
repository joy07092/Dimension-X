package com.example.wallpaperx;

import android.content.Context;
import android.widget.Toast;

import com.example.wallpaperx.Listeners.CuratedResponseListener;
import com.example.wallpaperx.Listeners.SearchResponseListener;
import com.example.wallpaperx.Models.CuratedApiResponse;
import com.example.wallpaperx.Models.SearchApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class RequestManager {
    Context context;  //object
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.pexels.com/v1/").addConverterFactory(GsonConverterFactory.create()).build(); //object
    //base url added for the api response and also the converter from Gson

    public RequestManager(Context context) {
        this.context = context;
    } //constructor

    public void getCuratedWallpapers(CuratedResponseListener listener, String page){   //method to access the interface from main activity
        CallWallpaperList callWallpaperList = retrofit.create(CallWallpaperList.class);   //listener is the interface curatedresponselistener
        Call<CuratedApiResponse> call = callWallpaperList.getwallpapers(page, "30");  //page is default 1 given in the pexels

        call.enqueue(new Callback<CuratedApiResponse>() {  //to send the request
            @Override
            public void onResponse(Call<CuratedApiResponse> call, Response<CuratedApiResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());  //pass the parameters for CuratedResponseListener
            }

            @Override
            public void onFailure(Call<CuratedApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());         //pass the parameters for CuratedResponseListener

            }
        });
    }

    public void searchCuratedWallpapers(SearchResponseListener listener, String page, String query){  //everything is same except listener and the query parameter
        CallWallpaperListSearch callWallpaperListSearch = retrofit.create(CallWallpaperListSearch.class);
        Call<SearchApiResponse> call = callWallpaperListSearch.searchwallpapers(query, page, "45");

        call.enqueue(new Callback<SearchApiResponse>() {
            @Override
            public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<SearchApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());

            }
        });
    }


    private interface CallWallpaperList {

        @Headers( {
                "Accept: application/json",  //to get the response in json form
                "Authorization: 563492ad6f917000010000018907eeec9ccc48399e53c6a391b30437"  //this is my api key
        } )
        @GET("curated/") //represent end-point of base url to get data
        Call<CuratedApiResponse> getwallpapers(  //Call the method for response "getwallpapers" and "CuratedApiResponse" is the model for which we wanna get the response
                @Query("page") String page,
                @Query("per_page") String per_page  //they are the parameters for the method
        );

    }
    private interface CallWallpaperListSearch {

        @Headers( {
                "Accept: application/json",
                "Authorization: 563492ad6f917000010000018907eeec9ccc48399e53c6a391b30437"
        } )
        @GET("search")  //represent end-point of base url to get data
        Call<SearchApiResponse> searchwallpapers( //Call the method for response "searchwallpapers" and "SearchApiResponse" is the model for which we wanna get the response
                @Query("query") String query,
                @Query("page") String page,             //they are the parameters
                @Query("per_page") String per_page
        );

    }
}
