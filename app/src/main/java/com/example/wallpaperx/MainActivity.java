package com.example.wallpaperx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wallpaperx.Adapters.CuratedAdapter;
import com.example.wallpaperx.Listeners.CuratedResponseListener;
import com.example.wallpaperx.Listeners.OnRecyclerClickListener;
import com.example.wallpaperx.Listeners.SearchResponseListener;
import com.example.wallpaperx.Models.CuratedApiResponse;
import com.example.wallpaperx.Models.Photo;
import com.example.wallpaperx.Models.SearchApiResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRecyclerClickListener {  //for getting the onClick method on the photo to go to WallpaperActivity

    FirebaseAuth authprofile;
    RecyclerView recyclerView_home;
    CuratedAdapter adapter;
    ProgressDialog dialog;
    RequestManager manager;   //objects
    FloatingActionButton fab_next, fab_prev;
    int page = 0, car1 = 0, nature1 = 0, sports1 = 0, animal1 = 0, food1 = 0, s1 = 0;   //page count
    Button car, nature, sports, animal, food, s;
    String Q;  //for the search query

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authprofile = FirebaseAuth.getInstance();

        fab_prev = findViewById(R.id.fab_prev);  //finding all the ids
        fab_next = findViewById(R.id.fab_next);

        car = findViewById(R.id.car);
        nature = findViewById(R.id.nature);
        sports = findViewById(R.id.sport);
        s = findViewById(R.id.s);
        animal = findViewById(R.id.animal);
        food = findViewById(R.id.food);          //finding all the ids
        s.setVisibility(4);                  //search button initially hidden

        dialog = new ProgressDialog(this);
        dialog.setTitle("Please Wait");             //creating and setting the progressdialog

        manager = new RequestManager(this);  //passing the context
        manager.getCuratedWallpapers(listener, "1");   //calling the method from requestmanager and the listener was created below

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car1 += 1;                   //only car page will update others will reset
                s1 = 0;
                nature1 = 0;
                sports1 = 0;
                animal1 = 0;
                food1 = 0;
                s.setVisibility(4);          //will hide the search button
                String next_page = String.valueOf(car1);    //taking the updated page number
                manager.searchCuratedWallpapers(searchResponseListener, next_page, "car");   //calling the method for search
                dialog.show();
            }
        });



        nature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nature1 += 1;               //only nature page will update others will reset
                s1 = 0;
                car1 = 0;
                animal1 = 0;
                food1 = 0;
                sports1 = 0;
                s.setVisibility(4);       //will hide the search button
                String next_page = String.valueOf(nature1);    //taking the updated page number
                manager.searchCuratedWallpapers(searchResponseListener, next_page, "nature");   //calling the method to search
                dialog.show();
            }
        });
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sports1 += 1;        //only sports page will update others will reset
                car1 = 0;
                animal1 = 0;
                food1 = 0;
                s1 = 0;
                nature1 = 0;
                s.setVisibility(4);          //will hide the search button
                String next_page = String.valueOf(sports1);   //taking the updated page number
                manager.searchCuratedWallpapers(searchResponseListener, next_page, "sports");  //calling the method to search
                dialog.show();
            }
        });

        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal1 += 1;        //only animal page will update others will reset
                sports1 = 0;
                car1 = 0;
                food1 = 0;
                s1 = 0;
                nature1 = 0;
                s.setVisibility(4);      //will hide the search button
                String next_page = String.valueOf(animal1);  //taking the updated page number
                manager.searchCuratedWallpapers(searchResponseListener, next_page, "animal");   //calling the method to search
                dialog.show();
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food1 += 1;            //only food page will update others will reset
                sports1 = 0;
                car1 = 0;
                animal1 = 0;
                s1 = 0;
                nature1 = 0;
                s.setVisibility(4);      //will hide the search button
                String next_page = String.valueOf(food1);    //taking the updated page number
                manager.searchCuratedWallpapers(searchResponseListener, next_page, "food"); //calling the method to search
                dialog.show();
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1 += 1;   //updating the page number
                String next_page = String.valueOf(s1);   //taking the updated page number
                manager.searchCuratedWallpapers(searchResponseListener, next_page, Q);  //calling the method to search and Q is the query string taken from onQueryTextSubmit onCreateOptionsMenu
                dialog.show();
            }
        });



        fab_next.setOnClickListener(new View.OnClickListener() {  //for random new page
            @Override
            public void onClick(View v) {
                car1 = 0;
                s1 = 0;
                animal1 = 0;
                food1 = 0;
                nature1 = 0;
                sports1 = 0;
                page += 1;                 //only the random counter will be upated and others reset
                s.setVisibility(4);       //will hide the search button
                String next_page = String.valueOf(page);
                manager.getCuratedWallpapers(listener, next_page);   //calling the method to get the new page
                dialog.show();
            }
        });

        fab_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car1 = 0;
                s1 = 0;
                animal1 = 0;
                food1 = 0;
                nature1 = 0;
                sports1 = 0;
                s.setVisibility(4);           //same as fab_next
                if(page > 1){
                    page -= 1;
                    String prev_page = String.valueOf(page);
                    manager.getCuratedWallpapers(listener, prev_page);
                    dialog.show();
                }
                else {   //to put a stop at the first page
                    Toast.makeText(MainActivity.this, "First page!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private final CuratedResponseListener listener = new CuratedResponseListener() {  //interface in listener
        @Override
        public void onFetch(CuratedApiResponse response, String message) {
            dialog.dismiss(); //for cancelling when new page is loaded
            if(response.getPhotos().isEmpty()){  //in the model curatedapiresponse
                Toast.makeText(MainActivity.this, "No image found!", Toast.LENGTH_SHORT).show();
                return;
            }
            page = response.getPage();   //we are gettng the page number

            showData(response.getPhotos());  //method to show the images by attaching the adapter to recycleview created below

        }

        @Override
        public void onError(String message) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };

    private void showData(List<Photo> photos) {

        recyclerView_home = findViewById(R.id.recycler_home);  //finding the recycleview
        recyclerView_home.setHasFixedSize(true);  //children having fixed length and width
        recyclerView_home.setLayoutManager(new GridLayoutManager(this, 3));  // setting the layout in the recycleview
        adapter = new CuratedAdapter(MainActivity.this, photos, this);  //initializing the adapter
        recyclerView_home.setAdapter(adapter); //attaching the adapter to recycleview

    }

    @Override    //OnRecyclerClickListener interface in listeners has it and this is why we have implemented it in AppCompatActivity
    public void onClick(Photo photo) {  //to get to new activity to set or download selected wallpaper
        startActivity(new Intent(MainActivity.this, WallpaperActivity.class)
                .putExtra("photo", photo));  //to send the particular photo we need to make the model (photo,src) serializable

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);  //
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Wallpaper");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Q = query;   //getting the query globally
                s.setVisibility(3);  //showing the button for more
                s1 += 1;   //updating the page number
                s.setText(Q);   //more button text set
                manager.searchCuratedWallpapers(searchResponseListener, "1", query);  //calling api method for searching
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.userinfo){
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.logout){
            authprofile.signOut();
            Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //top->If the activity being started is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it are destroyed and this intent is delivered to the resumed instance of the activity (now on top), through onNewIntent() ).
            startActivity(intent);  //Clear->can only be used with FLAG_ACTIVITY_NEW_TASK and it doesn't create a new task. All the activities in the current task are getting finished and the new activity(C in your case) becomes the root of the task.
            finish();  //new->if a task is already running for the activity you are now starting, then a new activity will not be started; instead, the current task will simply be brought to the front of the screen with the state it was last in.
        }else if(id == R.id.changepassword){
            Intent intent = new Intent(MainActivity.this, UpdatePasswordActivity.class);
            startActivity(intent);
        }else if(id == R.id.favorite){
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private final SearchResponseListener searchResponseListener = new SearchResponseListener() {  //same thing as the curated
        @Override
        public void onFetch(SearchApiResponse response, String message) {
            dialog.dismiss();
            if(response.getPhotos().isEmpty()){
                Toast.makeText(MainActivity.this, "No image found", Toast.LENGTH_SHORT).show();
                return;
            }
            showData(response.getPhotos());
        }

        @Override
        public void onError(String message) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };
}