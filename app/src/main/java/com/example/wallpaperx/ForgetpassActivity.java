package com.example.wallpaperx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgetpassActivity extends AppCompatActivity {

    Button button;
    EditText et;
    ProgressBar progressBar;
    FirebaseAuth authprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);

        getSupportActionBar().setTitle("Forgot password");
        button = findViewById(R.id.button_password_reset);
        et = findViewById(R.id.editText_password_reset_email);
        progressBar = findViewById(R.id.progressBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et.getText().toString();
                if(TextUtils.isEmpty(email)){  //toast messages
                    Toast.makeText(ForgetpassActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    et.setError("Email is required");
                    et.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(ForgetpassActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    et.setError("Valid email is required");
                    et.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword(email);  //method created below

                }
            }
        });
    }

    private void resetPassword(String email) {

        authprofile = FirebaseAuth.getInstance();
        authprofile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetpassActivity.this, "Check email for reset link", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgetpassActivity.this, WelcomeActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);  //see the menu activity logout option
                    finish();
                }else{
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){  //toast messages
                        et.setError("User doesn't exist!");
                        et.requestFocus();

                    }catch (Exception e){
                        Log.e("ForgetpassActivity", e.getMessage());
                        Toast.makeText(ForgetpassActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(ForgetpassActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}