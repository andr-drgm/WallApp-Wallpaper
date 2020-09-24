package com.example.wallappwallpaper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.WallPaperViewHolder> {

    IWallPaperDB wallpaperData;

    public static class WallPaperViewHolder extends RecyclerView.ViewHolder {
        public View dataView;
        public ImageView imageView;
        //public TextView cardTextView;

        public WallPaperViewHolder(View v)
        {
            super(v);
            dataView = v;

//            cardTextView = (TextView) dataView.findViewById(R.id.textView);
            imageView = (ImageView) dataView.findViewById(R.id.imageView);
        }
    }

    public WallPaperAdapter(IWallPaperDB dataSet)
    {
        wallpaperData = dataSet;
    }

    @Override
    public WallPaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View testDataView = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallpaper_row, parent, false);

        return new WallPaperViewHolder(testDataView);
    }

    @Override
    public void onBindViewHolder(WallPaperViewHolder holder, int position) {

        final WallPaper currentWallPaper = wallpaperData.get(position);

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(currentWallPaper.getImagePath());

        Glide
                .with(holder.dataView.getContext())
                .load(ref)
                .into(holder.imageView);



        holder.dataView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, WallPaperActivity.class);
                intent.putExtra("wallPaper", currentWallPaper);

                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return wallpaperData.size();
    }


}
