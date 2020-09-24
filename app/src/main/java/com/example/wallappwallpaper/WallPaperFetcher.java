package com.example.wallappwallpaper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class WallPaperFetcher {

    Service wallPaperService;

    public WallPaperFetcher(Service wallPaperService){
        this.wallPaperService = wallPaperService;
    }

    void PopulateLocal() throws Exception {

        wallPaperService.AddWallPaper(R.drawable.wall1,"@adr_drgm", "Descriere 1", "Titlu fain", "Nume1");
        wallPaperService.AddWallPaper(R.drawable.wall2,"@adr_drgm", "Descriere 2", "Titlu mai fain", "Nume2");
        wallPaperService.AddWallPaper(R.drawable.wall3,"@adr_drgm", "Descriere 3", "Titlu mult mai fain", "Nume3");
        wallPaperService.AddWallPaper(R.drawable.wall4,"@adr_drgm", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall5,"@adr_drgm", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall6,"@adr_drgm", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall7,"@beyond_the_forest", "Architecture", "Budapest Train Station", "Start journey");
        wallPaperService.AddWallPaper(R.drawable.wall8,"@beyond_the_forest", "History", "Hősök tere", "War chariot");
        wallPaperService.AddWallPaper(R.drawable.wall9,"@beyond_the_forest", "Mediaș downtown", "Mediaș", "Watchtower");
        wallPaperService.AddWallPaper(R.drawable.wall10,"@beyond_the_forest", "Constanța casino", "Constanța", "Old Casino");
        wallPaperService.AddWallPaper(R.drawable.wall11,"@beyond_the_forest", "Poiana  Brașov", "Ski slope", "Up we go");
        wallPaperService.AddWallPaper(R.drawable.wall12,"@beyond_the_forest", "Idk, thought he looked cool ;)", "Random spider", "Night hunter");
    }

    void PopulateServer() throws Exception
    {
        // Get data from firebase or something...
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("wallpapers");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot wallpaperShot : snapshot.getChildren())
                {
                    WallPaper wallpaper = wallpaperShot.getValue(WallPaper.class);

                    Log.i("TEST", String.valueOf(wallpaper.getAuthor()));
                    Log.i("TEST", String.valueOf(wallpaper.getDescription()));


                    try {
                        wallPaperService.AddWallPaper(wallpaper.getImagePath(), wallpaper.getAuthor(), wallpaper.getDescription(),
                                wallpaper.getTitle(),wallpaper.getName());
                    } catch (Exception e) {
                        Log.i("TEST", "Failed constructor");
                        e.printStackTrace();
                    }
                    //Log.i("TEST", wallpaper.getName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ///

            }
        });

    }



}
