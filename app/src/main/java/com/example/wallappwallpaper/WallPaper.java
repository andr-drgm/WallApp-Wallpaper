package com.example.wallappwallpaper;

import android.graphics.Bitmap;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WallPaper wallPaper = (WallPaper) o;
        return name.equals(wallPaper.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getAuthor() {
        return author;
    }

    public String getName(){
        return name;
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
