package com.example.wallappwallpaper;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WallPaperFetcher {

    Service wallPaperService;

    public WallPaperFetcher(Service wallPaperService){
        this.wallPaperService = wallPaperService;
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
                        wallPaperService.AddWallPaper(wallpaper.getImagePath(), wallpaper.getAuthor(), wallpaper.getDescription(),
                                wallpaper.getTitle(),wallpaper.getName(),wallpaper.getDownloads());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                wallPaperAdapter.setWallPaperDataFull(wallPaperService.GetAllWallPapers());
                wallPaperAdapter.notifyDataSetChanged();

                //mainActivityReference.autoChangeWallpaper();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
