package com.example.wallappwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WallPaperActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.wallpaper_activity);

        Intent intent = getIntent();
        WallPaper wallpaper = (WallPaper) intent.getSerializableExtra("wallPaper");

        Log.i("DEBUG", String.valueOf(wallpaper.getImagePath()));

        ImageView wallPaperImageView = findViewById(R.id.imageView2);
        wallPaperImageView.setImageResource(wallpaper.getImagePath());

    }


}
