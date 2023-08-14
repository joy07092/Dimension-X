package com.example.wallpaperx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordActivity extends AppCompatActivity {

    FirebaseAuth authprofile;
    EditText et1, et2;
    TextView authenticated;
    Button auth, changepass;
    ProgressBar progressBar;
    String currentpass;
    ImageView showhidepass1, showhidepass2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        getSupportActionBar().setTitle("Change-Password");
        et1 = findViewById(R.id.editText_change_pwd_current);
        et2 = findViewById(R.id.editText_change_pwd_new);
        authenticated = findViewById(R.id.textView_change_pwd_authenticated);
        auth = findViewById(R.id.button_change_pwd_authenticate);
        changepass = findViewById(R.id.button_change_pwd);
        progressBar = findViewById(R.id.progressBar);
        showhidepass1 = findViewById(R.id.imageView_show_hide_curr_pwd);
        showhidepass2 = findViewById(R.id.imageView_show_hide_new_pwd);

        et2.setEnabled(false);
        changepass.setEnabled(false);
        showhidepass2.setEnabled(false);  //the change part is disabled first

        showhidepass1.setImageResource(R.drawable.ic_hide_pwd);
        showhidepass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //show hide for pass authenticate
                if(et1.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    et1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showhidepass1.setImageResource(R.drawable.ic_show_pwd);
                }else{
                    et1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showhidepass1.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });
        showhidepass2.setImageResource(R.drawable.ic_hide_pwd);
        showhidepass2.setOnClickListener(new View.OnClickListener() { //show hide for pass new
            @Override
            public void onClick(View v) {
                if(et2.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showhidepass2.setImageResource(R.drawable.ic_show_pwd);
                }else{
                    et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showhidepass2.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });

        authprofile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authprofile.getCurrentUser();

        if(firebaseUser.equals(null)){  //if user is not found
            Toast.makeText(UpdatePasswordActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdatePasswordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            reAuthenticateUser(firebaseUser);  //method to reauthenticate created below
        }
    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentpass = et1.getText().toString();  //taking the current pass
                if(TextUtils.isEmpty(currentpass)){//toast message
                    Toast.makeText(UpdatePasswordActivity.this, "Enter current Password", Toast.LENGTH_SHORT).show();
                    et1.setError("Enter current password");
                    et1.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), currentpass);  //represents credential firebase use to authenticate user
                    firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){  //when user is authenticated
                                progressBar.setVisibility(View.GONE);
                                showhidepass1.setEnabled(false);
                                showhidepass2.setEnabled(true);
                                et1.setEnabled(false);
                                et2.setEnabled(true);
                                auth.setEnabled(false);
                                changepass.setEnabled(true);
                                authenticated.setText("You are authenticated");  //the authenticate part is disabled and change pass part is enabled
                                Toast.makeText(UpdatePasswordActivity.this, "Enter new password", Toast.LENGTH_SHORT).show();
                                changepass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePassword(firebaseUser);
                                    }  //method to change the pass
                                });
                            }else { //if not
                                try{
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(UpdatePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePassword(FirebaseUser firebaseUser) {
        String Newpass = et2.getText().toString();  //getting the new pass
        if(TextUtils.isEmpty(Newpass)){  //toast message
            Toast.makeText(UpdatePasswordActivity.this, "Enter new password", Toast.LENGTH_SHORT).show();
            et2.setError("Enter new password");
            et2.requestFocus();
        }else if(currentpass.matches(Newpass)){  //toast message
            Toast.makeText(UpdatePasswordActivity.this, "New and previous password can't be same", Toast.LENGTH_SHORT).show();
            et2.setError("Enter new password");
            et2.requestFocus();
        }else{  //all is well
            progressBar.setVisibility(View.VISIBLE);
            firebaseUser.updatePassword(Newpass).addOnCompleteListener(new OnCompleteListener<Void>() {  //updating the pass
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UpdatePasswordActivity.this, "Password is changed", Toast.LENGTH_SHORT).show();
                        et2.setEnabled(false);
                        changepass.setEnabled(false);   //all is disabled
                    }else{
                        if(Newpass.length() < 6){  //toast message
                            Toast.makeText(UpdatePasswordActivity.this, "At least 6 characters", Toast.LENGTH_SHORT).show();
                            et2.setError("Too weak");
                            et2.requestFocus();

                        }else{  //for exception
                            try{
                                throw task.getException();
                            }catch (Exception e){
                                Toast.makeText(UpdatePasswordActivity.this, "Something's wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }
}


