package com.adrw.wallappwallpaper.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adrw.wallappwallpaper.R;
import com.adrw.wallappwallpaper.Service;
import com.adrw.wallappwallpaper.WallPaper;
import com.adrw.wallappwallpaper.WallPaperAdapter;
import com.adrw.wallappwallpaper.WallPaperDB;
import com.adrw.wallappwallpaper.WallPaperFetcher;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private HashMap<WallPaper, Boolean> likedWallpapers;
    int tabIndex;
    WallPaperAdapter wallPaperAdapter;

    private PageViewModel pageViewModel;

    PlaceholderFragment(int index){
        tabIndex = index;
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        this.setArguments(bundle);
    }

    public void update()
    {
        wallPaperAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }


    public void SaveData(HashMap<WallPaper, Boolean> arrayList, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("wallApp:likedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
        Type type = new TypeToken<HashMap<WallPaper, Boolean>>(){}.getType();

        String json = gson.toJson(arrayList,type );
        editor.putString("Liked List", json);
        editor.apply();
    }

    public HashMap<WallPaper, Boolean> LoadData(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences("wallApp:likedPreferences", MODE_PRIVATE);
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

    public WallPaperAdapter getWallPaperAdapter() {
        return wallPaperAdapter;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.wallpapers_tab_list);
        WallPaperDB testDB = new WallPaperDB();
        final Service wallpaperService = new Service(testDB);
        final WallPaperFetcher wallPaperFetcher = new WallPaperFetcher(wallpaperService, root.getContext());
        recyclerView.setHasFixedSize(true);

        likedWallpapers = LoadData(root.getContext());

        GridLayoutManager layoutManager = new GridLayoutManager(root.getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        wallPaperAdapter = new WallPaperAdapter(testDB, likedWallpapers);
        recyclerView.setAdapter(wallPaperAdapter);

        switch (tabIndex){
            // All wallpapers
            case 0:
                Log.i("TEST", "test");
                try{
                    wallPaperFetcher.PopulateServer(wallPaperAdapter);
                }
                catch (Exception e ){
                    e.printStackTrace();
                }

                break;
                // Popular tab
            case 1:

                try{
                    wallPaperFetcher.PopulateServerSorted(wallPaperAdapter);
                }
                catch (Exception e ){
                    e.printStackTrace();
                }

                break;
                // Liked ( WIP )
            case 2:

                try{
                    wallPaperFetcher.PopulateServerLiked(wallPaperAdapter);
                }
                catch (Exception e ){
                    e.printStackTrace();
                }


                break;


        }


        return root;
    }



}