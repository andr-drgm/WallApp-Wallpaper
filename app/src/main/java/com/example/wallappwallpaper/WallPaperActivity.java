package com.example.wallappwallpaper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
/*
        wallPaperAuthorView.setOnClickListener( new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.youtube.com"));
                startActivity(intent);
            }

        });
*/


        wallPaperImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Change wallpaper");

                builder.setPositiveButton("Set wallpaper", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WallpaperManager wallpaperManager =
                                WallpaperManager.getInstance(getApplicationContext());
                        try{
                            wallpaperManager.setResource(wallpaper.getImagePath());
                            Toast.makeText(v.getContext(), "Wallpaper set", Toast.LENGTH_SHORT).show();

                            // Go to home screen
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);

                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

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
                //startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
                onBackPressed();
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
        assert wallpaper != null;
        wallPaperImageView.setImageResource(wallpaper.getImagePath());

    }

}

