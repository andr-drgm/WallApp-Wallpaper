package com.example.wallappwallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        testDB.Add(new WallPaper(R.drawable.wall4,"Author", "Descriere 4", "Titlu ceva acolo", "Nume4"));

        recyclerView = (RecyclerView) findViewById(R.id.wallpapers_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        wallPaperAdapter = new WallPaperAdapter(testDB.GetAllWallPapers());
        recyclerView.setAdapter(wallPaperAdapter);


    }
}