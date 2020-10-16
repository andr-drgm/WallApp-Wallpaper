package com.adrw.wallappwallpaper;

import android.net.Uri;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.Objects;

public class WallPaper implements Serializable,Cloneable {

    private final String imagePath;
    private final String author;
    private String description;
    private final String title;
    private final String name;
    private int    downloads;

    private String parsedUri;

    public WallPaper()
    {
        author = "";
        imagePath = "";
        title = "";
        name = "";
        downloads = 0;
        parsedUri = "";
    }

    public WallPaper(WallPaper other)
    {
        this.imagePath = other.imagePath;
        this.description = other.description;
        this.title = other.title;
        this.name = other.name;
        this.author = other.author;
        this.downloads = other.downloads;
        this.parsedUri = other.parsedUri;
    }

    public WallPaper(String imagePath, String author, String description, String title, String name){
        this.imagePath = imagePath;
        this.author = author;
        this.description = description;
        this.title = title;
        this.name = name;
        this.parsedUri = "";
    }

    public WallPaper(String imagePath, String author, String description, String title, String name, int downloads){
        this.imagePath = imagePath;
        this.author = author;
        this.description = description;
        this.title = title;
        this.name = name;
        this.downloads = downloads;
        this.parsedUri = "";
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

    public Uri getParsedUri(){
        return Uri.parse(this.parsedUri);
    }

    public void parseUri(){
        if(this.parsedUri.equals("")){
            final StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(getImagePath());
            Task<Uri> testTask = ref.getDownloadUrl();
            testTask.addOnSuccessListener(uri -> this.parsedUri = uri.toString());
        }
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
