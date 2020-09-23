package com.example.wallappwallpaper;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

public class Service {

    private IWallPaperDB wallPaperDB;

    public Service(IWallPaperDB myDatabase)
    {
        wallPaperDB = myDatabase;
    }

    public void AddWallPaper(int imagePath, String author, String description, String title, String name)
    {
        WallPaper newWallPaper = new WallPaper(imagePath, author, description, title, name);
        wallPaperDB.Add(newWallPaper);
    }

    public void RemoveWallPaper(int imagePath, String author, String description, String title, String name)
    {
        WallPaper newWallPaper = new WallPaper(imagePath, author, description, title, name);
        wallPaperDB.Remove(newWallPaper);
    }

    public void UpdateWallPaper(int imagePath, String author, String description, String title, String name,int newImagePath, String newAuthor, String newDescription, String newTitle, String newName)
    {
        WallPaper oldWallpaper = new WallPaper(imagePath, author, description, title, name);
        WallPaper newWallpaper = new WallPaper(newImagePath, newAuthor, newDescription, newTitle, newName);

        wallPaperDB.Update(oldWallpaper, newWallpaper);
    }





}
