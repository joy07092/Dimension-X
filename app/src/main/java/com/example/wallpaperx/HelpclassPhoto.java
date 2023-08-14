package com.example.wallpaperx;

public class HelpclassPhoto {//this is the class used for saving the info of favorite wallpaper in firebase

    public String photolink, name, id;

    public HelpclassPhoto(){};  //default constructor
    public HelpclassPhoto(String photolink, String name, String id) {
        this.photolink = photolink;
        this.name = name;
        this.id = id;
    }
}
