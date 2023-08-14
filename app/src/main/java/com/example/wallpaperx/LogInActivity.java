package com.example.wallpaperx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    EditText et1, et2;
    ProgressBar progressBar;
    FirebaseAuth authprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().setTitle("Login");   //setting the title actionbar
        et1 = findViewById(R.id.editText_login_email);
        et2 = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progressBar);

        authprofile = FirebaseAuth.getInstance();

        TextView registertext = findViewById(R.id.textView_register_link);
        TextView forgettext = findViewById(R.id.textView_forgot_password_link);
        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //going to register page
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);  //can come back
            }
        });

        forgettext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //going to forgot pass page and can come back
                Toast.makeText(LogInActivity.this, "Reset password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogInActivity.this, ForgetpassActivity.class));
            }
        });

        ImageView showhidepass = findViewById(R.id.imageView_show_hide_pwd);
        showhidepass.setImageResource(R.drawable.ic_hide_pwd);
        showhidepass.setOnClickListener(new View.OnClickListener() {  //for show and hide pass
            @Override
            public void onClick(View v) {
                if(et2.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showhidepass.setImageResource(R.drawable.ic_show_pwd);
                }else{
                    et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showhidepass.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });

        Button buttonlogin = findViewById(R.id.button_login);
        buttonlogin.setOnClickListener(new View.OnClickListener() {  //login
            @Override
            public void onClick(View v) {
                String textemail = et1.getText().toString();
                String textpwd = et2.getText().toString();  //getting the infos

                if(TextUtils.isEmpty(textemail)){  //toast message
                    Toast.makeText(LogInActivity.this,"Please enter your mail", Toast.LENGTH_SHORT).show();
                    et1.setError("Email is required");
                    et1.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textemail).matches()){  //toast message
                    Toast.makeText(LogInActivity.this,"Please enter your valid email", Toast.LENGTH_SHORT).show();
                    et1.setError("Valid email is required");
                    et1.requestFocus();
                }else if(TextUtils.isEmpty(textpwd)){  //toast message
                    Toast.makeText(LogInActivity.this,"Please enter your password", Toast.LENGTH_SHORT).show();
                    et2.setError("Password is required");
                    et2.requestFocus();
                }else{   //when all is well
                    progressBar.setVisibility(View.VISIBLE);
                    LoginUser(textemail, textpwd);   //method to login created below
                }
            }
        });
    }

    private void LoginUser(String textemail, String textpwd) {
        authprofile.signInWithEmailAndPassword(textemail, textpwd).addOnCompleteListener(LogInActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){  //sign in successful
                    FirebaseUser firebaseUser = authprofile.getCurrentUser();
                    if(firebaseUser.isEmailVerified()){  //when the email is verified
                        Toast.makeText(LogInActivity.this, "Logged in" , Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);  //going to main activity and not coming back
                        finish();
                    }else{  //if not
                        firebaseUser.sendEmailVerification();
                        authprofile.signOut();   //sign out the user
                        showAlertDialog();   //method to show created below
                        progressBar.setVisibility(View.GONE);
                    }

                }else{ //if not
                    Toast.makeText(LogInActivity.this, "     User doesn't exist\nInvalid mail or password" , Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
        builder.setTitle("Mail not verified");
        builder.setMessage("Verify Email otherwise you can't log in");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LogInActivity.this, "Go to your Email", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}