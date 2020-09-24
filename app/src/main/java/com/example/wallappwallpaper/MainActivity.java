package com.example.wallappwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter wallPaperAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private WallPaperDB testDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testDB = new WallPaperDB();
        Service wallPaperService = new Service(testDB);
        WallPaperFetcher wallPaperFetcher = new WallPaperFetcher(wallPaperService);

        /*try {
            wallPaperFetcher.PopulateLocal();
        } catch (Exception e) {
            //.makeText( getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/

        try {
            wallPaperFetcher.PopulateServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("TEST", String.valueOf(testDB.size()));

        UiModeManager uiManager = (UiModeManager) getApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
        uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);

        recyclerView = (RecyclerView) findViewById(R.id.wallpapers_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        wallPaperAdapter = new WallPaperAdapter(testDB);
        recyclerView.setAdapter(wallPaperAdapter);


    }
}