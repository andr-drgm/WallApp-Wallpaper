package com.example.wallappwallpaper;

import android.graphics.Bitmap;

public class WallPaper {

    private Bitmap imageBitmap;
    private String imagePath;

    private String author;
    private String description;
    private String title;
    private String name;

    public WallPaper(String imagePath, String author, String description, String title, String name){
        this.imagePath = imagePath;
        this.author = author;
        this.description = description;
        this.title = title;
        this.name = name;

    }

    public WallPaper(Bitmap imageBitmap, String author, String description, String title, String name){
        this.imageBitmap = imageBitmap;
        this.author = author;
        this.description = description;
        this.title = title;
        this.name = name;

    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

}
