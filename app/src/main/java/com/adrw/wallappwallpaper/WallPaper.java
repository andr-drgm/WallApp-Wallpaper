package com.adrw.wallappwallpaper;

import java.io.Serializable;
import java.util.Objects;

public class WallPaper implements Serializable,Cloneable {

    private final String imagePath;
    private final String author;
    private String description;
    private final String title;
    private final String name;
    private int    downloads;

    public WallPaper()
    {
        author = "";
        imagePath = "";
        title = "";
        name = "";
        downloads = 0;
    }

    public WallPaper(WallPaper other)
    {
        this.imagePath = other.imagePath;
        this.description = other.description;
        this.title = other.title;
        this.name = other.name;
        this.author = other.author;
        this.downloads = other.downloads;
    }

    public WallPaper(String imagePath, String author, String description, String title, String name){
        this.imagePath = imagePath;
        this.author = author;
        this.description = description;
        this.title = title;
        this.name = name;
        this.downloads = 0;
    }

    public WallPaper(String imagePath, String author, String description, String title, String name, int downloads){
        this.imagePath = imagePath;
        this.author = author;
        this.description = description;
        this.title = title;
        this.name = name;
        this.downloads = downloads;
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

    public int getDownloads(){
        return this.downloads;
    }

    public void setDownloads(int value){
        this.downloads = value;
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

    @Override
    public String toString() {
        return "WallPaper{" +
                "imagePath='" + imagePath + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", downloads=" + downloads +
                '}';
    }
}
