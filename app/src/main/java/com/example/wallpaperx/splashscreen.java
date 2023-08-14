package com.example.wallpaperx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.window.SplashScreen;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class splashscreen extends AppCompatActivity {
    ProgressBar pb;
    Timer timer;
    int i = 0;
    FirebaseAuth authprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        pb = findViewById(R.id.pb);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        authprofile = FirebaseAuth.getInstance();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(i < 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    pb.setProgress(i);
                    i++;
                }
                else if (authprofile.getCurrentUser() != null){  //when already logged in
                    timer.cancel();
                    Intent intent = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    timer.cancel();
                    Intent intent = new Intent(splashscreen.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 0, 20);
    }
}