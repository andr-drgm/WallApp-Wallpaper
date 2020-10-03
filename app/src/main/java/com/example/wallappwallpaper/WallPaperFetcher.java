package com.example.wallappwallpaper;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WallPaperFetcher {

    Service wallPaperService;
    List<String> urlList;
    Context context;

    public WallPaperFetcher(Service wallPaperService,Context context){
        this.wallPaperService = wallPaperService;
        urlList = new ArrayList<>();
        this.context = context;
    }

    void PopulateServer(final WallPaperAdapter wallPaperAdapter) throws Exception
    {
        // Get data from firebase or something...
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("wallpapers");


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wallPaperService.Clear();

                for(DataSnapshot wallpaperShot : snapshot.getChildren())
                {
                    WallPaper wallpaper = wallpaperShot.getValue(WallPaper.class);
                    try {
                        String wallpaperUrl = wallpaper.getImagePath();

                        wallPaperService.AddWallPaper(wallpaperUrl, wallpaper.getAuthor(), wallpaper.getDescription(),
                                wallpaper.getTitle(),wallpaper.getName(),wallpaper.getDownloads());

                        urlList.add(wallpaperUrl);
                        Log.i("TEST", "Done reading from database...");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                wallPaperAdapter.setWallPaperDataFull(wallPaperService.GetAllWallPapers());
                wallPaperAdapter.notifyDataSetChanged();

                SetupChangeWallpaper(urlList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void SetupChangeWallpaper(List<String> urlList)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean autoChangeSet = sharedPreferences.getBoolean("autoChange", false);

        if(autoChangeSet){
            // Setup alarm manager
            Log.i("TEST", "Setting up autochange");
            Random random = new Random();
            String randomUrl = urlList.get(random.nextInt(urlList.size()));

            Log.i("TEST", "Random url: " + randomUrl);

        }

    }



}
