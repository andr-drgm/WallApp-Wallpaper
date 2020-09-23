package com.example.wallappwallpaper;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.WallPaperViewHolder> {

    List<WallPaper> wallpaperData;
    public static class WallPaperViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public WallPaperViewHolder(TextView v)
        {
            super(v);
            textView = v;
        }
    }

    public WallPaperAdapter(List<WallPaper> dataSet)
    {
        wallpaperData = dataSet;
    }

    @Override
    public WallPaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView testTextView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallpaper_row, parent, false);

        WallPaperViewHolder wh = new WallPaperViewHolder(testTextView);
        return wh;
    }

    @Override
    public void onBindViewHolder(WallPaperViewHolder holder, int position) {
        holder.textView.setText(wallpaperData.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return wallpaperData.size();
    }


}
