package com.example.wallpaperx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    TextView et1, et2, et3, et5;
    ProgressBar progressBar;
    String name, mail, birthdate;
    ImageView imageView;
    FirebaseAuth authprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("User-Info");  //setting the tile
        et1 = findViewById(R.id.textView_show_full_name);
        et2 = findViewById(R.id.textView_show_email);
        et3 = findViewById(R.id.textView_show_dob);
        et5 = findViewById(R.id.textView_show_welcome);
        progressBar = findViewById(R.id.progress_bar);

        authprofile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authprofile.getCurrentUser();
        if(firebaseUser == null){  //no user found
            Toast.makeText(UserProfileActivity.this, "Details are not available at the moment", Toast.LENGTH_SHORT).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);  //method to see the info created below
        }
    }
    private void showUserProfile(FirebaseUser firebaseUser) {
        String UserId = firebaseUser.getUid();  //getting the id
        DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Registered users");  //extracting reference from firebase for "Registered users"
        referenceprofile.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {  //only see the child which match the id
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readuserdetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readuserdetails != null){
                    name = readuserdetails.name;
                    mail = readuserdetails.mail;
                    birthdate = readuserdetails.birth;  //getting all infos from firebase
                    et5.setText("Welcome "+name+"!");
                    et1.setText(name);
                    et2.setText(mail);
                    et3.setText(birthdate);   //setting the infos
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Details are not available at the moment", Toast.LENGTH_SHORT).show();

            }
        });
    }
}