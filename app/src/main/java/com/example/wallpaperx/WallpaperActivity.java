package com.example.wallpaperx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wallpaperx.Models.Photo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

public class WallpaperActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;  //later we'll need this to identify our request...we could choose any number besides 1
    Rect rect;                                  //1 means number 1 request
    ImageView imageView_wallpaper;
    FloatingActionButton fab_download, fab_favorite;
    Button fab_wallpaper;
    ProgressDialog dialog;
    Photo photo;  //photo object from my package
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        getSupportActionBar().hide();    //hiding the actionbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  //to get the full screen
        imageView_wallpaper = findViewById(R.id.imageView_wallpaper);
        fab_download = findViewById(R.id.fab_download);
        fab_favorite = findViewById(R.id.fab_favorite);
        fab_wallpaper = findViewById(R.id.fab_wallpaper);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Fav").child(firebaseUser.getUid());
        dialog = new ProgressDialog(this);
        dialog.setTitle("Please Wait");
        //dialog.show();
        photo = (Photo) getIntent().getSerializableExtra("photo");  //getting the photo

        Picasso.get().load(photo.getSrc().getOriginal()).placeholder(R.drawable.placeholder).into(imageView_wallpaper);  //loading the image
        //dialog.dismiss();
        fab_download.setOnClickListener(new View.OnClickListener() {  //for the download
            @Override
            public void onClick(View v) {
                dialog.show();
                if (ContextCompat.checkSelfPermission(WallpaperActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {  //when the permission is granted
                    DownloadManager downloadManager = null;  //downloadmanager = system service that handles long-running HTTP downloads
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                    Uri uri = Uri.parse(photo.getSrc().getOriginal());  //uri = sequence of the characters used to identify resources uniquely over the internet

                    DownloadManager.Request request = new DownloadManager.Request(uri);  //this class contains all the information necessary to request a new download.

                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)    //Roaming allows you to make calls, send texts, and use wireless data even when you're outside of your network's boundaries
                            .setTitle("Wallpaper_"+photo.getPhotographer())
                            .setMimeType("image/jpeg")   //indicate type of the file
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "Wallpaper_"+photo.getPhotographer()+".jpg");

                    downloadManager.enqueue(request);  //enqueue runs the request in background thread
                    dialog.dismiss();

                    Toast.makeText(WallpaperActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                } else { //when the permission is not granted
                    dialog.dismiss();
                    requestStoragePermission();  //this method gets the user permission
                }

            }
        });

        fab_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photoDatabaselink = photo.getSrc().getOriginal();
                String photoDatabasename = photo.getPhotographer();
                storeData(photoDatabaselink, photoDatabasename);

            }
        });

        fab_wallpaper.setOnClickListener(new View.OnClickListener() {  //to set the wallpaper from the app
            @Override
            public void onClick(View v) {
                dialog.show();
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(WallpaperActivity.this);
                Bitmap bitmap = ((BitmapDrawable) imageView_wallpaper.getDrawable()).getBitmap();   //bitmap is for handle the image
                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 300, 600, false);  //setting the size
                try {
                    wallpaperManager.setBitmap(bitmap1);
                    //wallpaperManager.setBitmap(bitmap1, null, true, WallpaperManager.FLAG_LOCK);
                    dialog.dismiss();
                    Toast.makeText(WallpaperActivity.this, "Wallpaper applied", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();  //will pinpoint the exact line in which the method raised the exception.
                    dialog.dismiss();
                    Toast.makeText(WallpaperActivity.this, "Couldn't set Wallpaper", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void storeData(String photoDatabaselink, String photoDatabasename) {


        String key = ref.push().getKey();


        HelpclassPhoto helpclassPhoto = new HelpclassPhoto(photoDatabaselink, photoDatabasename, key);
        ref.child(key).setValue(helpclassPhoto).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //when the user is created the email will be send
                    Toast.makeText(WallpaperActivity.this, "Saved to favorites", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(WallpaperActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {  //this method checks if show the dialog exaplains why we need the permission,true when aleady denied the permission before and try to access it again

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to download the wallpaper")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {   //will show box to give the permission or not
                            ActivityCompat.requestPermissions(WallpaperActivity.this,    //request the permissions needed
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {  //will just cancel the dialog box
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else { //will show box to give the permission or not
            ActivityCompat.requestPermissions(this,   //request the permission needed
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  { //we are only taking the request from the string array that matches our code
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { ///checking if the permission was given,,grantResults[0] only the first one in this case and grantResults.length > 0 to make sure array is not empty
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}