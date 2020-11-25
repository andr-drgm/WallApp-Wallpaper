package com.adrw.wallappwallpaper;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;

public class WallPaperActivity extends AppCompatActivity {

    private Button setWallpaperButton;
    private ImageView wallpaperImage;
    private CheckBox likeCheckbox;
    private HashMap<WallPaper, Boolean> likedMap;

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.wallpaper_activity);

        Intent intent = getIntent();
        final WallPaper wallpaper = (WallPaper) intent.getSerializableExtra("wallPaper");
        likedMap = (HashMap<WallPaper, Boolean>) intent.getSerializableExtra("exists");

        TextView wallPaperTitleTextView = findViewById(R.id.wall_title_text_view);
        final TextView wallPaperAuthorView = findViewById(R.id.wall_author_text_view);
        TextView wallPaperDescView = findViewById(R.id.wall_description_text_view);

        setWallpaperButton = findViewById(R.id.setWallpaper_button);
        wallpaperImage = findViewById(R.id.wall_image);
        likeCheckbox = findViewById(R.id.like_checkBox);

        final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(wallpaper.getImagePath());

        Task<Uri> testTask = ref.getDownloadUrl();

        testTask.addOnSuccessListener(uri -> Glide
                .with(getApplicationContext())
                .load(uri)
                .into(wallpaperImage));

        wallPaperTitleTextView.setText(wallpaper.getTitle());
        wallPaperAuthorView.setText(wallpaper.getAuthor());
        wallPaperDescView.setText(wallpaper.getDescription());

        // Set wallpaper code...
        wallPaperAuthorView.setOnClickListener(v -> {
            String val = wallPaperAuthorView.getText().toString();
            val = val.substring(1);
            Uri webpage = Uri.parse("https://instagram.com/" + val + "/");
            Intent intent12 = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(intent12);
        });

        likeCheckbox.setChecked( likedMap.containsKey(wallpaper));

        // Like checkbox
        likeCheckbox.setOnClickListener(v ->{

            if(!likedMap.containsKey(wallpaper)) {
                likedMap.put(wallpaper, true);
                Toast.makeText(getApplicationContext(), "LIKED", Toast.LENGTH_SHORT).show();

            }
            else{
                likedMap.remove(wallpaper);

            }


        });

        setWallpaperButton.setOnClickListener(v -> {

            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Change wallpaper");

            builder.setPositiveButton("Set wallpaper", (dialog, which) -> {
                // Update database for new download value
                final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("wallpapers");

                int downloads = wallpaper.getDownloads();
                wallpaper.setDownloads(downloads + 1);

                databaseRef.child(wallpaper.getName()).setValue(wallpaper).addOnCompleteListener(task -> {
                });

                // Get image from database storage
                final StorageReference ref1 = FirebaseStorage.getInstance().getReferenceFromUrl(wallpaper.getImagePath());
                Task<Uri> testTask1 = ref1.getDownloadUrl();
                testTask1.addOnSuccessListener(uri -> {
                    setWallpaperButton.setText(R.string.loading_set_wallpaper);
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(uri)
                            .dontTransform()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    Uri myImageUri = getImageUri(resource, getApplicationContext());
                                    Intent intent1 = getWallpaperSetterIntent(myImageUri);
                                    startActivity(Intent.createChooser(intent1, "Set as:"));
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                });
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });
    }

    private Intent getWallpaperSetterIntent(Uri myImageUri) {
        Intent intent1 = new Intent();
        intent1.setAction(WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER);
//                                                intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent1.setDataAndType(myImageUri, "image/*");
        intent1.putExtra("mimeType", "image/*");

        return intent1;
    }

    @Override
    public void onBackPressed() {
        SaveData(likedMap);
        super.onBackPressed();
    }

    public void SaveData(HashMap<WallPaper, Boolean> arrayList) {
        SharedPreferences sharedPreferences = getSharedPreferences("wallApp:likedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
        Type type = new TypeToken<HashMap<WallPaper, Boolean>>(){}.getType();

        String json = gson.toJson(arrayList,type );
        editor.putString("Liked List", json);
        editor.apply();
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

