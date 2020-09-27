package com.example.wallappwallpaper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.WallPaperViewHolder> implements Filterable {

    IWallPaperDB wallpaperData;
    List<WallPaper> wallPaperDataFull;

    HashMap<WallPaper, Boolean> likedMap;

    public static class WallPaperViewHolder extends RecyclerView.ViewHolder {
        private View dataView;
        private ImageView imageView;
        private TextView wallpaperTitle;
        private ProgressBar progressBar;
        private Button likeButton;
        //public TextView cardTextView;
        private HashMap<WallPaper, Boolean> likedMap;

        public WallPaperViewHolder(View v, HashMap<WallPaper, Boolean> likedList)
        {
            super(v);
            dataView = v;
            this.likedMap = likedList;

            likeButton = (Button) dataView.findViewById(R.id.like_button);
////            cardTextView = (TextView) dataView.findViewById(R.id.textView);
            imageView = (ImageView) dataView.findViewById(R.id.imageView);
            wallpaperTitle = (TextView) dataView.findViewById(R.id.row_title_textView);
            progressBar = (ProgressBar) dataView.findViewById(R.id.progressBar);
        }
    }

    public WallPaperAdapter(IWallPaperDB dataSet, HashMap<WallPaper, Boolean> likedMap)
    {
        wallpaperData = dataSet;
        wallPaperDataFull = new ArrayList<>(wallpaperData.GetAllWallPapers());
        this.likedMap = likedMap;
    }

    public void setWallPaperDataFull(List<WallPaper> wallPapers){
        this.wallPaperDataFull = new ArrayList<>(wallPapers);
    }

    public List<WallPaper> GetWallpaperList(){
        return this.wallPaperDataFull;
    }

    @Override
    public WallPaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View testDataView = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallpaper_row, parent, false);

        return new WallPaperViewHolder(testDataView,this.likedMap);
    }

    @Override
    public void onBindViewHolder(final WallPaperViewHolder holder, final int position) {

        final WallPaper currentWallPaper = wallpaperData.get(position);

        if(this.likedMap.containsKey(currentWallPaper)){
            holder.likeButton.setText("Liked");
        }
        else{
            holder.likeButton.setText("Like");
        }


        holder.wallpaperTitle.setText(currentWallPaper.getTitle());

        final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(currentWallPaper.getImagePath());

        Task<Uri> testTask = ref.getDownloadUrl();

        testTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide
                        .with(holder.dataView.getContext())
                        .load(uri)
                        .into(holder.imageView);

                holder.progressBar.setVisibility(View.GONE);
            }
        });

        holder.dataView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, WallPaperActivity.class);
                intent.putExtra("wallPaper", currentWallPaper);

                context.startActivity(intent);

            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likedMap.containsKey(currentWallPaper))
                {
                    likedMap.remove(currentWallPaper);
                }
                else {
                    likedMap.put(currentWallPaper, true);

                }
                notifyItemChanged(position);
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
                    if( wallPaper.getTitle().toLowerCase().contains(filterPattern)){
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
