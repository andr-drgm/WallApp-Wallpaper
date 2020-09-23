package com.example.wallappwallpaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.WallPaperViewHolder> {

    List<WallPaper> wallpaperData;
    public static class WallPaperViewHolder extends RecyclerView.ViewHolder {
        public View dataView;

        public TextView titleView;
        public TextView descriptionView;
        public ImageView imageView;

        public WallPaperViewHolder(View v)
        {
            super(v);
            dataView = v;
            titleView = (TextView) dataView.findViewById(R.id.title_textView);
            descriptionView = (TextView) dataView.findViewById(R.id.description_textView);
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
        holder.titleView.setText(wallpaperData.get(position).getTitle());
        holder.descriptionView.setText(wallpaperData.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return wallpaperData.size();
    }


}
