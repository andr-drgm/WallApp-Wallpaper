package com.example.wallappwallpaper;

import java.util.List;

public class Service {

    private IWallPaperDB wallPaperDB;
    private WallPaperValidator wallPaperValidator;

    public Service(IWallPaperDB myDatabase)
    {
        wallPaperDB = myDatabase;
        wallPaperValidator = new WallPaperValidator();
    }

    public Service()
    {
        wallPaperDB = new WallPaperDB();
        wallPaperValidator = new WallPaperValidator();

    }

    public void AddWallPaper(WallPaper wallPaper){
        wallPaperDB.Add(wallPaper);
    }
    public List<WallPaper> GetAllWallPapers(){
        return this.wallPaperDB.GetAllWallPapers();
    }

    public void AddWallPaper(String imagePath, String author, String description, String title, String name) throws Exception {

        WallPaper newWallPaper = new WallPaper(imagePath, author, description, title, name);
        wallPaperValidator.validate(newWallPaper);
        wallPaperDB.Add(newWallPaper);
    }

    public void AddWallPaper(String imagePath, String author, String description, String title, String name,int downloads) throws Exception {

        WallPaper newWallPaper = new WallPaper(imagePath, author, description, title, name,downloads);
        wallPaperValidator.validate(newWallPaper);
        wallPaperDB.Add(newWallPaper);
    }

    public void RemoveWallPaper(String name) throws Exception {
        WallPaper newWallPaper = new WallPaper("", "", "", "", name);
        wallPaperValidator.validate(newWallPaper);
        wallPaperDB.Remove(newWallPaper);
    }

    public void UpdateWallPaper(String name,String newImagePath, String newAuthor, String newDescription, String newTitle, String newName) throws Exception {

        WallPaper oldWallpaper = new WallPaper("", "", "", "", name);
        WallPaper newWallpaper = new WallPaper(newImagePath, newAuthor, newDescription, newTitle, newName);
        wallPaperValidator.validate(newWallpaper);
        wallPaperDB.Update(oldWallpaper, newWallpaper);
    }

}
