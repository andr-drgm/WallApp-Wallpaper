package com.adrw.wallappwallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class WallpaperAutoChange extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        Toast.makeText(context, "Wallpaper set", Toast.LENGTH_SHORT).show();
        String url = intent.getStringExtra("url");
        Log.i("UrlImage", url);


                final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url);

                Task<Uri> testTask = ref.getDownloadUrl();

                testTask.addOnSuccessListener(uri -> Glide.with(context)
                        .asBitmap()
                        .load(uri)
                        .into(new SimpleTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                try {
                                    Toast.makeText(context, "O fost setat", Toast.LENGTH_SHORT).show();
                                    WallpaperManager.getInstance(context).setBitmap(resource);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }));


                Toast.makeText(context, "Wallpaper set", Toast.LENGTH_SHORT).show();

    }
}
