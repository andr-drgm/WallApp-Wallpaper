package com.example.wallappwallpaper;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        wallPaperAuthorView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = wallPaperAuthorView.getText().toString();
                val = val.substring(1);
                Uri webpage = Uri.parse("https://instagram.com/" + val + "/");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });


        setWallpaperButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Change wallpaper");

                builder.setPositiveButton("Set wallpaper", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        // Update database for new download value
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("wallpapers");


                        DatabaseReference wallPaperDownloads = databaseRef.child(wallpaper.getName()).child("downloads");

                        wallPaperDownloads.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int downloads = snapshot.getValue(int.class);
                                wallpaper.setDownloads(downloads + 1);
                                Log.i("TEST", "" + wallpaper.getDownloads());

                                databaseRef.child(wallpaper.getName()).setValue(wallpaper).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.i("TEST", "Successfully updated wallpaper");
                                        } else {
                                            Log.i("TEST", "Failed updating wallpaper");

                                        }
                                    }

                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        // Get image from database storage
                        final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(wallpaper.getImagePath());
                        Task<Uri> testTask = ref.getDownloadUrl();

                        testTask.addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(final Uri uri) {

                                setWallpaperButton.setText("Loading...");

                                Glide.with(getApplicationContext())
                                        .asBitmap()
                                        .load(uri)
                                        .dontTransform()
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                Uri myImageUri = getImageUri(resource, getApplicationContext());
                                                Intent intent = new Intent();
                                                intent.setAction(WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER);
//                                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                                intent.setDataAndType(myImageUri, "image/*");
                                                intent.putExtra("mimeType", "image/*");
                                                startActivity(Intent.createChooser(intent, "Set as:"));
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }

                                        });
                            }
                        });

                        // Go to home screen
//                        Intent startMain = new Intent(Intent.ACTION_MAIN);
//                        startMain.addCategory(Intent.CATEGORY_HOME);
//                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(startMain);

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

