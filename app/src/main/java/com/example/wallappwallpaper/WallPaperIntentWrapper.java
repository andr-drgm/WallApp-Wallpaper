package com.example.wallappwallpaper;

import java.io.Serializable;
import java.util.ArrayList;

public class WallPaperIntentWrapper implements Serializable {

    private ArrayList<WallPaper> wallPapers;

    public WallPaperIntentWrapper()
    {
        wallPapers = new ArrayList<>();
    }

    public ArrayList<WallPaper> getWallPaperList()
    {
        return wallPapers;
    }

    public void setWallPapers(ArrayList<WallPaper> wallPapers)
    {
        this.wallPapers = wallPapers;
    }




}
