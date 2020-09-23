package com.example.wallappwallpaper;

import android.graphics.Bitmap;

public class WallPaper {

    private Bitmap imageBitmap;
    private String imagePath;

    private String author;
    private String description;
    private String title;

    public WallPaper(String imagePath, String author, String description, String title){
        this.imagePath = imagePath;
        this.author = author;
        this.description = description;
        this.title = title;

    }

    public WallPaper(Bitmap imageBitmap, String author, String description, String title){
        this.imageBitmap = imageBitmap;
        this.author = author;
        this.description = description;
        this.title = title;

    }


}
