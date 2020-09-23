package com.example.wallappwallpaper;

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

import java.util.List;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.WallPaperViewHolder> {

    List<WallPaper> wallpaperData;
    public static class WallPaperViewHolder extends RecyclerView.ViewHolder {
        public View dataView;
        public ImageView imageView;

        public WallPaperViewHolder(View v)
        {
            super(v);
            dataView = v;
            imageView = (ImageView) dataView.findViewById(R.id.imageView);
        }
    }

    public WallPaperAdapter(List<WallPaper> dataSet)
    {
        wallpaperData = dataSet;
    }

    @Override
    public WallPaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View testDataView = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallpaper_row, parent, false);

        WallPaperViewHolder wh = new WallPaperViewHolder(testDataView);
        return wh;
    }

    @Override
    public void onBindViewHolder(WallPaperViewHolder holder, int position) {
//          wallpaperData.get(position).getImagePath()
//        Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem() , wallpaperData.get(position).getImagePath());
//        holder.imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bm, 200, 200));
        holder.imageView.setImageResource(wallpaperData.get(position).getImagePath());
    }

    @Override
    public int getItemCount() {
        return wallpaperData.size();
    }


}
