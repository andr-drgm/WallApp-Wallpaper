package com.example.wallappwallpaper;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class WallPaperActivity extends AppCompatActivity {

    private Button setWallpaperButton;
    private ImageView wallpaperImage;

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.wallpaper_activity);

        Intent intent = getIntent();
        final WallPaper wallpaper = (WallPaper) intent.getSerializableExtra("wallPaper");

        TextView wallPaperTitleTextView = findViewById(R.id.wall_title_text_view);
        final TextView wallPaperAuthorView = findViewById(R.id.wall_author_text_view);
        TextView wallPaperDescView = findViewById(R.id.wall_description_text_view);

        setWallpaperButton = findViewById(R.id.setWallpaper_button);
        wallpaperImage = findViewById(R.id.wall_image);

        final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(wallpaper.getImagePath());

        Task<Uri> testTask = ref.getDownloadUrl();

        testTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide
                        .with(getApplicationContext())
                        .load(uri)
                        .into(wallpaperImage);
            }
        });

        wallPaperTitleTextView.setText(wallpaper.getTitle());
        wallPaperAuthorView.setText(wallpaper.getAuthor());
        wallPaperDescView.setText(wallpaper.getDescription());

        // Set wallpaper code...
         wallPaperAuthorView.setOnClickListener( new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                String val = wallPaperAuthorView.getText().toString();
                val = val.substring(1);
                Uri webpage = Uri.parse("https://instagram.com/" + val + "/");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }});



        setWallpaperButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Change wallpaper");

                builder.setPositiveButton("Set wallpaper", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(wallpaper.getImagePath());
                        Task<Uri> testTask = ref.getDownloadUrl();

                        testTask.addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(final Uri uri) {


                                Glide.with(getApplicationContext())
                                        .asBitmap()
                                        .load(uri)
                                        .into(new SimpleTarget<Bitmap>(){

                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                try {
                                                    WallpaperManager.getInstance(getApplicationContext()).setBitmap(resource);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                            }
                        });


                        Toast.makeText(v.getContext(), "Wallpaper set", Toast.LENGTH_SHORT).show();

                        // Go to home screen
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);

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

//        BACK BUTTON
//        Button backButton = findViewById(R.id.wall_back_button);
//        backButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //Context context = v.getContext();
//                //Intent intent = new Intent(Intent.ACTION_MAIN);
//                //intent.addCategory(Intent.CATEGORY_HOME);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                //context.startActivity(intent);
//                //startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
//                onBackPressed();
//            }
//        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.wallpaper_activity);
    }

    private Uri getImageUri(Bitmap inImage, Context inContext) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

}

