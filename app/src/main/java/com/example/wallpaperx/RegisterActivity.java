package com.example.wallpaperx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    EditText fullname,email,birthdate,pass;
    ProgressBar progressBar;
    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Registration");  //setting the title of actionbar
        Toast.makeText(RegisterActivity.this, "You can register now!", Toast.LENGTH_SHORT).show();
        fullname = findViewById(R.id.editText_register_full_name);
        email = findViewById(R.id.editText_register_email);
        birthdate = findViewById(R.id.editText_register_dob);
        pass = findViewById(R.id.editText_register_password);
        progressBar = findViewById(R.id.progressBar);

        ImageView showhidepass = findViewById(R.id.imageView_show_hide_pwd);  //show or hide the pass
        showhidepass.setImageResource(R.drawable.ic_hide_pwd);
        showhidepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //method to show or hide pass
                if(pass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showhidepass.setImageResource(R.drawable.ic_show_pwd);  //changing the image also
                }else{
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showhidepass.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });

        birthdate.setOnClickListener(new View.OnClickListener() {  //datepicker will popup when this is clicked
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);  //month starts from 0
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button register = findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {  //register button
            @Override
            public void onClick(View v) {
                String name = fullname.getText().toString();
                String mail = email.getText().toString();
                String birth = birthdate.getText().toString();
                String password = pass.getText().toString();   //getting all the info from the edittexts

                if(TextUtils.isEmpty(name)){//see the toast text
                    Toast.makeText(RegisterActivity.this,"Please enter your full name", Toast.LENGTH_SHORT).show();
                    fullname.setError("Full name is required");
                    fullname.requestFocus();
                }else if(TextUtils.isEmpty(mail)){//see the toast text
                    Toast.makeText(RegisterActivity.this,"Please enter your email", Toast.LENGTH_SHORT).show();
                    email.setError("Email is required");
                    email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){//see the toast text
                    Toast.makeText(RegisterActivity.this,"Please enter your valid email", Toast.LENGTH_SHORT).show();
                    email.setError("Valid email is required");
                    email.requestFocus();
                }else if(TextUtils.isEmpty(birth)){//see the toast text
                    Toast.makeText(RegisterActivity.this,"Please enter your birthdate", Toast.LENGTH_SHORT).show();
                    birthdate.setError("Birthdate is required");
                    birthdate.requestFocus();
                }else if(TextUtils.isEmpty(password)){//see the toast text
                    Toast.makeText(RegisterActivity.this,"Please enter your password", Toast.LENGTH_SHORT).show();
                    pass.setError("Password is required");
                    pass.requestFocus();
                }else if(password.length() < 6){//see the toast text
                    Toast.makeText(RegisterActivity.this,"Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                    pass.setError("Password is too weak");
                    pass.requestFocus();
                }else{  //when all info are given correctly
                    progressBar.setVisibility(View.VISIBLE);  //progressbar will start
                    RegisterUser(name, mail, birth,password);  //method for registration created below, all the info are passed
                }

            }
        });
    }

    private void RegisterUser(String name, String mail, String birth, String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();  //for authentication and we have used email, FirebaseAuth is the entry point of Firebase Authentication SDK
        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {//when the user is authenticated
                if(task.isSuccessful()){  //when successful

                    FirebaseUser firebaseUser = auth.getCurrentUser();  //getting the current user's profile

                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(name, mail, birth);  //class for saving and fetching the data from firebase

                    DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Registered users");//getting the instance from the reference
                    referenceprofile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() { //under the ref->under the id->the info are saved
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {//when the infos are saved
                            if(task.isSuccessful()){
                                firebaseUser.sendEmailVerification();  //when the user is created the email will be send
                                Toast.makeText(RegisterActivity.this, "User has been created", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });


                }else{
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){  //invalid email
                        email.setError("Mail is invalid or already in use");
                        email.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    }catch (FirebaseAuthUserCollisionException e){  //error message
                        email.setError("Mail already in use");
                        email.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    }catch (Exception e){ //all others
                        Log.e("RegisterActivity", e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}