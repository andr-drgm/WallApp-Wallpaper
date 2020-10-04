package com.example.wallappwallpaper;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO: needs serious refactoring since the populate function is the same thing copy-pasted 3 times...

public class WallPaperFetcher {

    Service wallPaperService;
    List<String> urlList;
    Context context;

    public WallPaperFetcher(Service wallPaperService,Context context){
        this.wallPaperService = wallPaperService;
        urlList = new ArrayList<>();
        this.context = context;
    }

    public void PopulateServerSorted(final WallPaperAdapter wallPaperAdapter){
        // Get data from firebase or something...
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("wallpapers");


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wallPaperService.Clear();

                List<WallPaper> wallpapers = getDataFromDatabase(snapshot);
                for(WallPaper wallPaper: wallpapers){
                    wallPaperService.AddWallPaper(wallPaper);
                }

                Collections.sort(wallPaperService.GetAllWallPapers(), (lhs, rhs) -> Integer.compare(rhs.getDownloads(), lhs.getDownloads()));

                wallPaperAdapter.setWallPaperDataFull(wallPaperService.GetAllWallPapers());
                wallPaperAdapter.notifyDataSetChanged();

                SetupChangeWallpaper(urlList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void PopulateServer(final WallPaperAdapter wallPaperAdapter) throws Exception
    {
        // Get data from firebase or something...
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("wallpapers");


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wallPaperService.Clear();

                List<WallPaper> wallpapers = getDataFromDatabase(snapshot);
                for(WallPaper wallPaper: wallpapers){
                    wallPaperService.AddWallPaper(wallPaper);
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

    List<WallPaper> getDataFromDatabase(DataSnapshot snapshot){
        List<WallPaper> wallpaperList = new ArrayList<>();

        for(DataSnapshot wallpaperShot : snapshot.getChildren())
        {
            WallPaper wallpaper = wallpaperShot.getValue(WallPaper.class);
            wallpaperList.add(wallpaper);
        }

        return wallpaperList;
    }

    public void PopulateServerLiked(final WallPaperAdapter wallPaperAdapter) throws Exception
    {
        // Get data from firebase or something...
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("wallpapers");


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wallPaperService.Clear();

                List<WallPaper> wallpapers = getDataFromDatabase(snapshot);
                for(WallPaper wallPaper: wallpapers){
                    wallPaperService.AddWallPaper(wallPaper);
                }

                wallPaperAdapter.setWallPaperDataFull(wallPaperService.GetAllWallPapers());
                wallPaperAdapter.getLikedFilter().filter("");

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

        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean autoChangeSet = sharedPreferences.getBoolean("autoChange", false);

        if(autoChangeSet){
            // Setup alarm manager
            Log.i("TEST", "Setting up autochange");
            Random random = new Random();
            String randomUrl = urlList.get(random.nextInt(urlList.size()));

            Log.i("TEST", "Random url: " + randomUrl);

        }*/

    }



}
