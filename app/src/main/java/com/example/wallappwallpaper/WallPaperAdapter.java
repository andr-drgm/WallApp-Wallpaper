package com.example.wallappwallpaper;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

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

    public List<WallPaper> GetWallPaperData()
    {
        return wallpaperData;
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

        final WallPaper currentWallpaper = wallpaperData.get(position);

        holder.imageView.setImageResource(currentWallpaper.getImagePath());

       /* holder.dataView.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( this, WallPaperActivity.class);

                startActivity(intent);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return wallpaperData.size();
    }


}
