package com.example.wallpaperx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    ListView listView;
    DatabaseReference ref;
    List<HelpclassPhoto> list;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getSupportActionBar().setTitle("Favorites");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();  //getting the user
        ref = FirebaseDatabase.getInstance().getReference("Fav").child(firebaseUser.getUid());  //ref is used that way cause we wanna store distinct favorites for individual user
        listView = findViewById(R.id.listview);
        list = new ArrayList<>();
        adapter = new Adapter(FavoriteActivity.this, list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //when we click item in listview
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HelpclassPhoto r = list.get(position);
                startActivity(new Intent(FavoriteActivity.this, FavoriteWallpaperActivity.class).putExtra("photo", r.photolink));
                Intent intent = new Intent(FavoriteActivity.this, FavoriteWallpaperActivity.class);
                intent.putExtra("photolink", r.photolink);
                intent.putExtra("photoname", r.name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //to handle null pointer exception
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {  //for deletion
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int which = position;
                which = which + 2;  //because there was ArrayIndexOutOfBoundsException
                new AlertDialog.Builder(FavoriteActivity.this)
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle("Are you sure?")
                        .setMessage("Do you really want to delete this favorite?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HelpclassPhoto r = list.get(position);
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                DatabaseReference ref1;
                                ref1 = FirebaseDatabase.getInstance().getReference("Fav").child(firebaseUser.getUid()).child(r.id);  //getting the specific reference for the deletion
                                ref1.removeValue();
                                startActivity(new Intent(FavoriteActivity.this, FavoriteActivity.class));
                                finish();


                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        list.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HelpclassPhoto r = dataSnapshot.getValue(HelpclassPhoto.class);
                    list.add(r);
                }
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }
}