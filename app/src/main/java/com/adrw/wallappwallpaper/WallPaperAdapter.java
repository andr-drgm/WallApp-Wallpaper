package com.adrw.wallappwallpaper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adrw.wallappwallpaper.ui.main.PlaceholderFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.WallPaperViewHolder> implements Filterable {

    IWallPaperDB wallpaperData;
    List<WallPaper> wallPaperDataFull;

    HashMap<WallPaper, Boolean> likedMap;
    HashMap<String, Uri> uriMap;
    HashMap< Integer, PlaceholderFragment> cachedFragments;

    public static class WallPaperViewHolder extends RecyclerView.ViewHolder {
        private final View dataView;
        private final ImageView imageView;
        private final TextView wallpaperTitle;
        private final ProgressBar progressBar;
        private final CheckBox likeCheckBox;

        public WallPaperViewHolder(View v)
        {
            super(v);
            dataView = v;

            likeCheckBox = dataView.findViewById(R.id.like_button);
            imageView = dataView.findViewById(R.id.imageView);
            wallpaperTitle = dataView.findViewById(R.id.row_title_textView);
            progressBar = dataView.findViewById(R.id.progressBar);
        }
    }

    public WallPaperAdapter(IWallPaperDB dataSet, HashMap<WallPaper, Boolean> likedMap, HashMap< Integer, PlaceholderFragment> cachedFragments,HashMap<String, Uri> uriMap)
    {
        wallpaperData = dataSet;
        wallPaperDataFull = new ArrayList<>(wallpaperData.GetAllWallPapers());
        this.likedMap = likedMap;
        //uriMap = new HashMap<>();
        this.cachedFragments = cachedFragments;
        this.uriMap = uriMap;

    }

    public void setLikedMap(HashMap<WallPaper, Boolean> likedMap){
        this.likedMap = likedMap;
    }

    public void setWallPaperDataFull(List<WallPaper> wallPapers){
        this.wallPaperDataFull = new ArrayList<>(wallPapers);
    }

    public List<WallPaper> GetWallPaperDataFull(){
        return this.wallPaperDataFull;
    }

    public List<WallPaper> GetWallpaperList(){
        return this.wallPaperDataFull;
    }

    @NonNull
    @Override
    public WallPaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View testDataView = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallpaper_row, parent, false);


        return new WallPaperViewHolder(testDataView);
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

    @Override
    public void onBindViewHolder(final WallPaperViewHolder holder, final int position) {

        final WallPaper currentWallPaper = wallpaperData.get(position);

        holder.likeCheckBox.setChecked(likedMap.containsKey(currentWallPaper));
        holder.wallpaperTitle.setText(currentWallPaper.getTitle());

            if (!uriMap.containsKey(currentWallPaper.getImagePath())) {
                final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(currentWallPaper.getImagePath());
                Task<Uri> testTask = ref.getDownloadUrl();

                testTask.addOnSuccessListener(uri -> {
                    Glide
                            .with(holder.dataView.getContext())
                            .load(uri)
                            .into(holder.imageView);

                    holder.progressBar.setVisibility(View.GONE);
                    uriMap.put(currentWallPaper.getImagePath(), uri);
                });
            } else {
                Glide
                        .with(holder.dataView.getContext())
                        .load(uriMap.get(currentWallPaper.getImagePath()))
                        .into(holder.imageView);
                holder.progressBar.setVisibility(View.GONE);

            }
/*

            Glide
                    .with(holder.dataView.getContext())
                    .load(currentWallPaper.getParsedUri())
                    .into(holder.imageView);
*/


        holder.dataView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, WallPaperActivity.class);
            intent.putExtra("wallPaper", currentWallPaper);
            intent.putExtra("exists",    likedMap);

            context.startActivity(intent);

        });

        holder.likeCheckBox.setOnClickListener(v -> {
            if(likedMap.containsKey(currentWallPaper))
            {
                // TODO: Change this text
                Toast.makeText(holder.dataView.getContext(), "Removed liked wallpaper",Toast.LENGTH_SHORT).show();
                likedMap.remove(currentWallPaper);

            }
            else {
                likedMap.put(currentWallPaper, true);
                Toast.makeText(holder.dataView.getContext(), "Liked wallpaper",Toast.LENGTH_SHORT).show();

            }

            SaveData(likedMap, holder.dataView.getContext());

            notifyItemChanged(position);
            for(PlaceholderFragment fragment : this.cachedFragments.values())
            {
                fragment.update(currentWallPaper);
                //fragment.getWallPaperAdapter().notifyDataSetChanged();
            }

        });

    }

    @Override
    public int getItemCount() {
        return wallpaperData.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public Filter getDownloadFilter()
    {
        return downloadFilter;
    }
    public Filter getLikedFilter(){return likedFilter;}

    public Filter likedFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            IWallPaperDB filteredDb = new WallPaperDB();
            for(WallPaper wallPaper: wallPaperDataFull){
                if( likedMap.containsKey(wallPaper)){
                    filteredDb.Add(wallPaper);

                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredDb.GetAllWallPapers();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            wallpaperData.clear();
            // ...
            if(results.values != null) {
                wallpaperData.GetAllWallPapers().addAll((List) results.values);
                notifyDataSetChanged();
            }
        }
    };

    public Filter downloadFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            IWallPaperDB filteredDb = new WallPaperDB();
            if(constraint == null || constraint.toString().length() == 0)
            {
                filteredDb.GetAllWallPapers().addAll(wallPaperDataFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                int downloads = Integer.parseInt(filterPattern);
                for(WallPaper wallPaper: wallPaperDataFull){

                    if( wallPaper.getDownloads() >= downloads){
                        filteredDb.Add(wallPaper);

                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredDb.GetAllWallPapers();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            wallpaperData.clear();
            // ...
            wallpaperData.GetAllWallPapers().addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            IWallPaperDB filteredDb = new WallPaperDB();
            if(constraint == null || constraint.toString().length() == 0)
            {
                filteredDb.GetAllWallPapers().addAll(wallPaperDataFull);

            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(WallPaper wallPaper: wallPaperDataFull){
                    if( wallPaper.getTitle().toLowerCase().contains(filterPattern) || wallPaper.getAuthor().toLowerCase().contains(filterPattern)){
                        filteredDb.Add(wallPaper);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredDb.GetAllWallPapers();

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            wallpaperData.clear();
            // ...
            wallpaperData.GetAllWallPapers().addAll((List)results.values);
            notifyDataSetChanged();

        }

    };


}
