package com.example.wallappwallpaper;

import java.util.List;

public interface IWallPaperDB {
    List<WallPaper> GetAllWallPapers();
    WallPaper GetWallPaperByName(String name);
    void Add(WallPaper wallPaper);
    boolean Remove(WallPaper wallPaper);
    boolean Update(WallPaper oldWallPaper, WallPaper newWallPaper);
    WallPaper get(int index);
    int size();
    void clear();
}
