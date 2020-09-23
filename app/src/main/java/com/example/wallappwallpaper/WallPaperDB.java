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
    public List<WallPaper> GetAllWallPapers() {
        return wallpaperList;
    }

    @Override
    public WallPaper GetWallPaperByName(String name) {
        for( WallPaper wallPaper : wallpaperList)
        {
            if(wallPaper.getName().equals(name))
            {
                return wallPaper;
            }
        }

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

        String oldWallpaperName = oldWallPaper.getName();

        for(int i =0;i<wallpaperList.size();++i)
        {
            if(wallpaperList.get(i).getName().equals(oldWallpaperName))
            {
                wallpaperList.set(i, newWallPaper);
                return true;
            }
        }

        return false;

    }

}
