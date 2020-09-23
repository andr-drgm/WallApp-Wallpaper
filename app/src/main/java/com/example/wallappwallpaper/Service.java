package com.example.wallappwallpaper;

import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

public class Service {

    private IWallPaperDB wallPaperDB;

    public Service(IWallPaperDB myDatabase)
    {
        wallPaperDB = myDatabase;
    }

    public void AddWallPaper(ImageView imagePath, String author, String description, String title, String name)
    {
        WallPaper newWallPaper = new WallPaper(imagePath, author, description, title, name);
        wallPaperDB.Add(newWallPaper);
    }

    public void RemoveWallPaper(ImageView imagePath, String author, String description, String title, String name)
    {
        WallPaper newWallPaper = new WallPaper(imagePath, author, description, title, name);
        wallPaperDB.Remove(newWallPaper);
    }





}
