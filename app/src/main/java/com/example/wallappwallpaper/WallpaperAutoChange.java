package com.example.wallappwallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class WallpaperAutoChange extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://wallapp-wallpaper.appspot.com/wall1.jpg");
        Task<Uri> testTask = ref.getDownloadUrl();

        testTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .asBitmap()
                        .load(uri)
                        .into(new SimpleTarget<Bitmap>(){

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                try {
                                    WallpaperManager.getInstance(context).setBitmap(resource);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });


        WallpaperManager wallpaperManager =
                WallpaperManager.getInstance(context);


        Toast.makeText(context, "Wallpaper set", Toast.LENGTH_SHORT).show();

    }
}
