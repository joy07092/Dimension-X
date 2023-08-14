package com.example.wallpaperx;

public class ReadWriteUserDetails {

    public String name, mail, birth;

    public ReadWriteUserDetails(){};  //default constructor
    public ReadWriteUserDetails(String name, String mail, String birth) {
        this.name = name;
        this.mail = mail;
        this.birth = birth;
    }
}
