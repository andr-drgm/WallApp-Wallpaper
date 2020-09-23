package com.example.wallappwallpaper;

import java.util.ArrayList;
import java.util.List;

public class WallPaperDB implements IWallPaperDB {

    List<WallPaper> wallpaperList;

    public WallPaperDB()
    {
        wallpaperList = new ArrayList<>();
    }

    @Override
    public List<WallPaper> getAllWallPapers() {
        return null;
    }

    @Override
    public void Add(WallPaper wallPaper) {
        wallpaperList.add(wallPaper);
    }

    @Override
    public boolean Remove(WallPaper wallPaper) {
        if(wallpaperList.contains(wallPaper))
            return wallpaperList.remove(wallPaper);
        else{
            return false;
        }
    }

    @Override
    public boolean Update(WallPaper oldWallPaper, WallPaper newWallPaper) {
        return false;
    }

}
