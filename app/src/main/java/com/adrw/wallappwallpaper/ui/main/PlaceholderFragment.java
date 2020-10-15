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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    int tabIndex;
    WallPaperAdapter wallPaperAdapter;
    private HashMap< Integer, PlaceholderFragment> cachedFragments;
    private RecyclerView recyclerView;
    private WallPaperDB testDB;
    private WallPaperFetcher wallPaperFetcher;
    private  HashMap<WallPaper, Boolean> likedWallpapers;

    PlaceholderFragment(int index,HashMap< Integer, PlaceholderFragment> cachedFragments ){
        tabIndex = index;
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        this.setArguments(bundle);
        this.cachedFragments = cachedFragments;
    }

    // TODO: Find better solution than wallPaperAdapter.notifyDataSetChanged();
    public void update()
    {
        if(wallPaperAdapter != null) {
            Context context = getContext();
            if(context != null)
                likedWallpapers = LoadData(context);

            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                wallPaperAdapter.setLikedMap(likedWallpapers);
                wallPaperAdapter.notifyDataSetChanged();

                if(tabIndex == 2){
                    wallPaperAdapter.setWallPaperDataFull(wallPaperAdapter.GetWallPaperDataFull());
                    wallPaperAdapter.getLikedFilter().filter("");

                    wallPaperAdapter.notifyDataSetChanged();
                }

                }
            );
        }
    }

    public void update(WallPaper wallpaper)
    {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {

            HashMap<WallPaper, Boolean> likedWallpapers = LoadData(getContext());
            wallPaperAdapter.setLikedMap(likedWallpapers);
            List<WallPaper> wallPapers = wallPaperAdapter.GetWallpaperList();

            for(int i =0;i<wallPapers.size();++i){
                if(wallPapers.get(i).equals(wallpaper)){
                    wallPaperAdapter.notifyItemChanged(i);
                }
            }

            if(tabIndex == 2){
                wallPaperAdapter.setWallPaperDataFull(wallPaperAdapter.GetWallPaperDataFull());
                wallPaperAdapter.getLikedFilter().filter("");

                wallPaperAdapter.notifyDataSetChanged();

            }


        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        PageViewModel pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
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
    public void onResume() {

        for(PlaceholderFragment fragment: this.cachedFragments.values()){
            fragment.update();
        }

        super.onResume();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = root.findViewById(R.id.wallpapers_tab_list);
        testDB = new WallPaperDB();
        final Service wallpaperService = new Service(testDB);
        wallPaperFetcher = new WallPaperFetcher(wallpaperService, root.getContext());
        recyclerView.setHasFixedSize(true);

        likedWallpapers = LoadData(root.getContext());

        GridLayoutManager layoutManager = new GridLayoutManager(root.getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        wallPaperAdapter = new WallPaperAdapter(testDB, likedWallpapers, cachedFragments);
        recyclerView.setAdapter(wallPaperAdapter);

        switch (tabIndex){
            // All wallpapers
            case 0:
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