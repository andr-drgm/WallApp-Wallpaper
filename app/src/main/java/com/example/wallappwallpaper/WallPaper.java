package com.example.wallappwallpaper;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.Objects;

public class WallPaper implements Serializable {

    private String imagePath;

    private String author;
    private String description;
    private String title;
    private String name;

    public WallPaper()
    {
        author = "";
        imagePath = "";
        title = "";
        name = "";

    }


    public WallPaper(String imagePath, String author, String description, String title, String name){
        this.imagePath = imagePath;
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

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

}
