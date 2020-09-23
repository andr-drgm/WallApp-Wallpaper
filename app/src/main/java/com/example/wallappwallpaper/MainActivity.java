package com.example.wallappwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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

        //int imagePath, String author, String description, String title, String name
        wallPaperService.AddWallPaper(R.drawable.wall1,"Author", "Descriere 1", "Titlu fain", "Nume1");
        wallPaperService.AddWallPaper(R.drawable.wall2,"Author2", "Descriere 2", "Titlu mai fain", "Nume2");
        wallPaperService.AddWallPaper(R.drawable.wall3,"Author3", "Descriere 3", "Titlu mult mai fain", "Nume3");
        wallPaperService.AddWallPaper(R.drawable.wall4,"Author4", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall5,"Author5", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall6,"Author6", "Descriere 4", "Titlu ceva acolo", "Nume4");

        UiModeManager uiManager = (UiModeManager) getApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
        uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);

        recyclerView = (RecyclerView) findViewById(R.id.wallpapers_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        wallPaperAdapter = new WallPaperAdapter(testDB.GetAllWallPapers());
        recyclerView.setAdapter(wallPaperAdapter);


    }
}