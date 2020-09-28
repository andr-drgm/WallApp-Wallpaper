package com.example.wallappwallpaper;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Enum for tab names
    private enum TabType{
        ALL_WALLPAPERS_TAB,
        POPULAR_TAB,
        LIKED_TAB
    }

    private RecyclerView recyclerView;
    private WallPaperAdapter wallPaperAdapter;
    private GridLayoutManager layoutManager;
    private WallPaperDB testDB;
    private FirebaseAuth mAuth;

    private int currentTabIndex;


    private HashMap<WallPaper, Boolean> likedWallpapers;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tab_layout);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null){
            signInAnonymously();
        }
        currentTabIndex = 0;

        // Liked list
        likedWallpapers = LoadData();

        testDB = new WallPaperDB();
        final Service wallPaperService = new Service(testDB);
        final WallPaperFetcher wallPaperFetcher = new WallPaperFetcher(wallPaperService);

        UiModeManager uiManager = (UiModeManager) getApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
        uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);

        recyclerView = findViewById(R.id.wallpapers_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        wallPaperAdapter = new WallPaperAdapter(testDB, likedWallpapers);
        recyclerView.setAdapter(wallPaperAdapter);


        // Getting wallpapers
        try {
            wallPaperFetcher.PopulateServer((WallPaperAdapter) wallPaperAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }



        recyclerView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {

            @Override
            public void onSwipeRight() {
                int nextTab = (currentTabIndex -1  ) % 4;
                if(nextTab < 0)
                {
                    nextTab = 0;
                }

                currentTabIndex = nextTab;

                tabLayout.getTabAt(nextTab).select();
                //Toast.makeText(getApplicationContext(), "right" + nextTab, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                int nextTab = (currentTabIndex +1 ) % 3;
                currentTabIndex = nextTab;

                tabLayout.getTabAt(nextTab).select();
                //Toast.makeText(getApplicationContext(), "left " + nextTab, Toast.LENGTH_SHORT).show();
            }


        });

         tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
             @Override
             public void onTabSelected(TabLayout.Tab tab) {
                 TabType tabtype = TabType.values()[tab.getPosition()];

                 switch(tabtype){
                     case ALL_WALLPAPERS_TAB:
                         AllWallpapersTab();
                         break;
                     case POPULAR_TAB:

                         PopularWallpapersTab();

                         break;

                     case LIKED_TAB:
                         LikedTab();
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

    // Tabs
    private void AllWallpapersTab(){
        wallPaperAdapter.getDownloadFilter().filter("");
    }

    private void PopularWallpapersTab()
    {
        ArrayList<WallPaper> fullDBList = new ArrayList<>(wallPaperAdapter.GetWallpaperList());
        testDB.SetAllWallPapers(fullDBList);

        Collections.sort(testDB.GetAllWallPapers(), (lhs, rhs) -> Integer.compare(rhs.getDownloads(), lhs.getDownloads()));

        wallPaperAdapter.notifyDataSetChanged();

    }

    private void LikedTab(){
        wallPaperAdapter.getLikedFilter().filter("");
        SaveData(likedWallpapers);
    }

    // end tabs



    public void SaveData(HashMap<WallPaper, Boolean> arrayList)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("wallApp:likedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
        Type type = new TypeToken<HashMap<WallPaper, Boolean>>(){}.getType();

        String json = gson.toJson(arrayList,type );
        editor.putString("Liked List", json);
        editor.apply();
    }

    public HashMap<WallPaper, Boolean> LoadData()
    {

        SharedPreferences sharedPreferences = getSharedPreferences("wallApp:likedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Liked List", null);
        java.lang.reflect.Type type = new TypeToken<HashMap<WallPaper, Boolean> >() {}.getType();
        HashMap<WallPaper, Boolean> loadedData;

        try {
            loadedData = gson.fromJson(json, type);
        }
        catch (com.google.gson.JsonSyntaxException e ){
            loadedData = new HashMap<>();
        }
        if(loadedData == null)
        {
            loadedData = new HashMap<>();
        }

        return loadedData;
    }


    // Search view and other menu stuff...
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

    private void signInAnonymously()
    {
        mAuth.signInAnonymously().addOnSuccessListener(this, authResult -> {
            // ok cool
            //Log.i("TEST", "Sign in successful: "+ authResult.getUser() );
        })
                .addOnFailureListener(this, e -> Log.e("TEST", "Firebase signin failed", e));
    }



}