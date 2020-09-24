package com.example.wallappwallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WallpaperAutoChange extends BroadcastReceiver {

    ArrayList<WallPaper> wallPaperList;
    WallPaper randomWallPaper;

    @Override
    public void onReceive(final Context context, Intent intent) {

        // FIXME: CRAHES APP...
        Random random = new Random();
        Bundle bundle = intent.getExtras();
        Log.i("TEST", "bundle list...?: " + String.valueOf(bundle.getSerializable("list")));

        if(bundle.getSerializable("list") != null)
        {
            Log.i("TEST", "Bundle: " + String.valueOf(bundle));
            Log.i("TEST", "Bundle ser: " + String.valueOf(bundle.getSerializable("list")));

            WallPaperIntentWrapper intentWrapper = (WallPaperIntentWrapper) bundle.getSerializable("list");
            Log.i("TEST", "CALLING ONRECEIVE");

            assert  intentWrapper != null;
            Log.i("TEST", "Intent wrapper: " + String.valueOf(intentWrapper));

            wallPaperList = new ArrayList<>();
            wallPaperList = intentWrapper.getWallPaperList();

            Log.i("TEST", String.valueOf( wallPaperList.size()));

            if(wallPaperList != null) {
                randomWallPaper = wallPaperList.get(random.nextInt(wallPaperList.size()));
                Log.i("TEST", "Calling auto...");


                final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(randomWallPaper.getImagePath());
                Task<Uri> testTask = ref.getDownloadUrl();


                testTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .asBitmap()
                                .load(uri)
                                .into(new SimpleTarget<Bitmap>() {

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
    }
}
