package com.adrw.wallappwallpaper;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.adrw.wallappwallpaper.ui.main.PlaceholderFragment;
import com.adrw.wallappwallpaper.ui.main.SectionsPagerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    TabLayout tabs;
    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously();

        setContentView(R.layout.activity_main2);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("wallpapers");

        HashMap<String, Uri> uriMap = new HashMap<>();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.i("TEST", "Permission granted");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        }


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot wallpaperShot : snapshot.getChildren()) {
                    WallPaper wallpaper = wallpaperShot.getValue(WallPaper.class);
                    if (wallpaper != null) {
                        if (!uriMap.containsKey(wallpaper.getImagePath())) {
                            final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(wallpaper.getImagePath());
                            Task<Uri> testTask = ref.getDownloadUrl();
                            testTask.addOnSuccessListener(uri -> uriMap.put(wallpaper.getImagePath(), uri));
                        }
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        viewPager = findViewById(R.id.view_pager);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), uriMap);
        viewPager.setAdapter(sectionsPagerAdapter);

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setFocusable(true);
        searchView.setIconified(false);

        searchView.setIconifiedByDefault(false);
        searchView.requestFocusFromTouch();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //wallPaperAdapter.getFilter().filter(newText);
                WallPaperAdapter wallpaperAdapter = ((PlaceholderFragment)sectionsPagerAdapter.getmCurrentFragment()).getWallPaperAdapter();
                wallpaperAdapter.getFilter().filter(newText);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                this.startActivity(intent);

                return true;

            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                this.startActivity(settingsIntent);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}