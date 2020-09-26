package com.example.wallappwallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WallPaperAdapter wallPaperAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private WallPaperDB testDB;
    private FirebaseAuth mAuth;

    // Tab layout stuff
    private TabLayout tabLayout;
    private TabItem allWallpaperTab;
    private TabItem popularWallpaperTab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        testDB = new WallPaperDB();
        final Service wallPaperService = new Service(testDB);
        final WallPaperFetcher wallPaperFetcher = new WallPaperFetcher(wallPaperService);

        UiModeManager uiManager = (UiModeManager) getApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
        uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);

        recyclerView = findViewById(R.id.wallpapers_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        wallPaperAdapter = new WallPaperAdapter(testDB);
        recyclerView.setAdapter(wallPaperAdapter);

        // Getting wallpapers
        try {
            wallPaperFetcher.PopulateServer((WallPaperAdapter) wallPaperAdapter,this);
        } catch (Exception e) {
            e.printStackTrace();
        }


         TabLayout tabLayout = findViewById(R.id.tab_layout);
         TabItem allWallpaperTab = findViewById(R.id.allWallpaperTab);
         TabItem popularWallpaperTab = findViewById(R.id.popularWallpaperTab);

         tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 switch(tab.getPosition()){
                     case 0:
                         wallPaperAdapter.getDownloadFilter().filter("");
                         break;
                     case 1:
                         Collections.sort(testDB.GetAllWallPapers(), new Comparator<WallPaper>() {
                             @Override
                             public int compare(WallPaper lhs, WallPaper rhs) {
                                 // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                 return Integer.compare(rhs.getDownloads(), lhs.getDownloads());
                             }
                         });

                            wallPaperAdapter.notifyDataSetChanged();
                         //wallPaperAdapter.getDownloadFilter().filter("3");
                         break;
                     default:

                 }
             }

             @Override
             public void onTabUnselected(TabLayout.Tab tab) {

             }

             @Override
             public void onTabReselected(TabLayout.Tab tab) {

             }
         });

    }

    // Search view and other menu stuff...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wallPaperAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.about:
                Toast.makeText(this,"About action...", Toast.LENGTH_SHORT).show();
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

    public void autoChangeWallpaper(){

        Intent intent = new Intent(MainActivity.this, WallpaperAutoChange.class);

        ArrayList<String> myUrls = new ArrayList<>();

        for(WallPaper wallPaper : testDB.GetAllWallPapers()){
            String url = wallPaper.getImagePath();
            myUrls.add(url);
        }

        intent.putStringArrayListExtra("list", myUrls);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long timeAtStart = System.currentTimeMillis();
        long tenSecondsInMillis = 1000 * 10;

        alarmManager.set(AlarmManager.RTC_WAKEUP, tenSecondsInMillis, pendingIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}