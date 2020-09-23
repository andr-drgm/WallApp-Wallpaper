package com.example.wallappwallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class WallPaperActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.wallpaper_activity);

        Intent intent = getIntent();
        final WallPaper wallpaper = (WallPaper) intent.getSerializableExtra("wallPaper");

        ImageView wallPaperImageView = findViewById(R.id.imageView2);
        TextView wallPaperTitleTextView = findViewById(R.id.wall_title_text_view);
        TextView wallPaperAuthorView = findViewById(R.id.wall_author_text_view);
        TextView wallPaperDescView = findViewById(R.id.wall_description_text_view);

        Button backButton = findViewById(R.id.wall_back_button);

        wallPaperImageView.setImageResource(wallpaper.getImagePath());
        wallPaperTitleTextView.setText(wallpaper.getTitle());
        wallPaperAuthorView.setText(wallpaper.getAuthor());
        wallPaperDescView.setText(wallpaper.getDescription());

        // Set wallpaper code...

        wallPaperImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                WallpaperManager wallpaperManager =
                        WallpaperManager.getInstance(getApplicationContext());
                try{
                    wallpaperManager.setResource(wallpaper.getImagePath());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        ///


        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Context context = v.getContext();
                //Intent intent = new Intent(Intent.ACTION_MAIN);
                //intent.addCategory(Intent.CATEGORY_HOME);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //context.startActivity(intent);
                startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.wallpaper_activity);

        Intent intent = getIntent();
        WallPaper wallpaper = (WallPaper) intent.getSerializableExtra("wallPaper");

        ImageView wallPaperImageView = findViewById(R.id.imageView2);
        wallPaperImageView.setImageResource(wallpaper.getImagePath());

    }




}
