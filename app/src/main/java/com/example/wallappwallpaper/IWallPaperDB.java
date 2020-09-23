package com.example.wallappwallpaper;

import java.util.List;

public interface IWallPaperDB {
    public List<WallPaper> getAllWallPapers();
    public void Add(WallPaper wallPaper);
    public boolean Remove(WallPaper wallPaper);
    public boolean Update(WallPaper oldWallPaper, WallPaper newWallPaper);

}
