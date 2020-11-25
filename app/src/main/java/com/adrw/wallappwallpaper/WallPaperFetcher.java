package com.adrw.wallappwallpaper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// TODO: needs serious refactoring since the populate function is the same thing copy-pasted 3 times...

public class WallPaperFetcher {

    private final Service wallPaperService;
    private final List<String> urlList;
    private final Context context;

    public WallPaperFetcher(Service wallPaperService, Context context) {
        this.wallPaperService = wallPaperService;
        urlList = new ArrayList<>();
        this.context = context;
    }

    private void SetupWallpaperService(final DataSnapshot snapshot) {
        wallPaperService.Clear();
        getDataFromDatabase(snapshot).forEach(v -> {
            wallPaperService.AddWallPaper(v);
            urlList.add(v.getImagePath());
        });
    }

    private void SetupWallpaperAdapter(final WallPaperAdapter wallPaperAdapter) {
        wallPaperAdapter.setWallPaperDataFull(wallPaperService.GetAllWallPapers());
        wallPaperAdapter.notifyDataSetChanged();

        SetupChangeWallpaper(urlList);
    }

    public void PopulateServerSorted(final WallPaperAdapter wallPaperAdapter) {
        // Get data from firebase or something...
        FirebaseDatabase.getInstance().getReference("wallpapers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SetupWallpaperService(snapshot);
                wallPaperService.GetAllWallPapers().sort((lhs, rhs) -> Integer.compare(rhs.getDownloads(), lhs.getDownloads()));
                SetupWallpaperAdapter(wallPaperAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public void PopulateServer(final WallPaperAdapter wallPaperAdapter) throws Exception
    {
        // Get data from firebase or something...
        FirebaseDatabase.getInstance().getReference("wallpapers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SetupWallpaperService(snapshot);
                SetupWallpaperAdapter(wallPaperAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private List<WallPaper> getDataFromDatabase(DataSnapshot snapshot) {
        List<DataSnapshot> snapShotList = StreamSupport.stream(snapshot.getChildren().spliterator(), false)
                .collect(Collectors.toList());

        return snapShotList.stream()
                .map(v -> v.getValue(WallPaper.class))
                .collect(Collectors.toList());
    }

    public void PopulateServerLiked(final WallPaperAdapter wallPaperAdapter) throws Exception
    {
        // Get data from firebase or something...
        FirebaseDatabase.getInstance().getReference("wallpapers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SetupWallpaperService(snapshot);

                SetupWallpaperAdapter(wallPaperAdapter);
                wallPaperAdapter.getLikedFilter().filter("");
                wallPaperAdapter.notifyDataSetChanged();

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
            Random random = new Random();
            String randomUrl = urlList.get(random.nextInt(urlList.size()));

            // AutoChangeWallpaper
            Intent intent = new Intent(context, WallpaperAutoChange.class);
            intent.putExtra("url", randomUrl);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // Set the alarm to start at approximately 2:00 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 1);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            Log.i("TEST", "Random url: " + randomUrl);

        }



    }



}
