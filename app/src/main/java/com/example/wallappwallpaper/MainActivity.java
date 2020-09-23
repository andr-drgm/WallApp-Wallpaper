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
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testDB = new WallPaperDB();

        imageView = findViewById(R.id.imageView);

        //int imagePath, String author, String description, String title, String name
        testDB.Add(new WallPaper(R.drawable.wall1,"Author", "Descriere 1", "Titlu fain", "Nume1"));
        testDB.Add(new WallPaper(R.drawable.wall2,"Author2", "Descriere 2", "Titlu mai fain", "Nume2"));
        testDB.Add(new WallPaper(R.drawable.wall3,"Author3", "Descriere 3", "Titlu mult mai fain", "Nume3"));
        testDB.Add(new WallPaper(R.drawable.wall4,"Author4", "Descriere 4", "Titlu ceva acolo", "Nume4"));
        testDB.Add(new WallPaper(R.drawable.wall5,"Author5", "Descriere 4", "Titlu ceva acolo", "Nume4"));
        testDB.Add(new WallPaper(R.drawable.wall6,"Author6", "Descriere 4", "Titlu ceva acolo", "Nume4"));
        //testDB.Add(new WallPaper(R.drawable.wall7,"Author", "Descriere 4", "Titlu ceva acolo", "Nume4"));
        //testDB.Add(new WallPaper(R.drawable.wall8,"Author", "Descriere 4", "Titlu ceva acolo", "Nume4"));
        //testDB.Add(new WallPaper(R.drawable.wall9,"Author", "Descriere 4", "Titlu ceva acolo", "Nume4"));

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