package com.example.wallpaperx.Models;

import java.io.Serializable;

public class Src implements Serializable { //cause for the implementation is we are sending particular photo object to another activity to use it later
    public String original;
    public String large2x;
    public String large;
    public String medium;
    public String small;
    public String portrait;
    public String landscape;
    public String tiny;

    public String getOriginal() {
        return original;
    }               //getter method to return the value of the attribute

    public String getLarge2x() {
        return large2x;
    }

    public String getLarge() {
        return large;
    }

    public String getMedium() {
        return medium;
    }

    public String getSmall() {
        return small;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getLandscape() {
        return landscape;
    }

    public String getTiny() {
        return tiny;
    }
}
