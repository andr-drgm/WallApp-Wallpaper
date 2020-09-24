package com.example.wallappwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter wallPaperAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private WallPaperDB testDB;
    private FirebaseAuth mAuth;

    private WallPaperAlarm wallPaperAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        testDB = new WallPaperDB();
        Service wallPaperService = new Service(testDB);
        WallPaperFetcher wallPaperFetcher = new WallPaperFetcher(wallPaperService);

        UiModeManager uiManager = (UiModeManager) getApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
        uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);

        recyclerView = findViewById(R.id.wallpapers_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        wallPaperAdapter = new WallPaperAdapter(testDB);
        recyclerView.setAdapter(wallPaperAdapter);

        // Getting wallpapers
        try {
            wallPaperFetcher.PopulateServer((WallPaperAdapter) wallPaperAdapter,this);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void autoChangeWallpaper(){

        Intent intent = new Intent(MainActivity.this, WallpaperAutoChange.class);

        ArrayList<String> myUrls = new ArrayList<>();

        for(WallPaper wallPaper : testDB.GetAllWallPapers()){
            String url = wallPaper.getImagePath();
            myUrls.add(url);
        }

        intent.putStringArrayListExtra("list", myUrls);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long timeAtStart = System.currentTimeMillis();
        long tenSecondsInMillis = 1000 * 10;

        alarmManager.set(AlarmManager.RTC_WAKEUP, tenSecondsInMillis, pendingIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}