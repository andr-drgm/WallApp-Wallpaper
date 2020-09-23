package com.example.wallappwallpaper;

public class WallPaperFetcher {

    Service wallPaperService;

    public WallPaperFetcher(Service wallPaperService){
        this.wallPaperService = wallPaperService;
    }

    void PopulateLocal()
    {
        wallPaperService.AddWallPaper(R.drawable.wall1,"Author", "Descriere 1", "Titlu fain", "Nume1");
        wallPaperService.AddWallPaper(R.drawable.wall2,"Author2", "Descriere 2", "Titlu mai fain", "Nume2");
        wallPaperService.AddWallPaper(R.drawable.wall3,"Author3", "Descriere 3", "Titlu mult mai fain", "Nume3");
        wallPaperService.AddWallPaper(R.drawable.wall4,"Author4", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall5,"Author5", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall6,"Author6", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall7,"Author6", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall8,"Author6", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall9,"Author6", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall10,"Author6", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall11,"Author6", "Descriere 4", "Titlu ceva acolo", "Nume4");
        wallPaperService.AddWallPaper(R.drawable.wall12,"Author6", "Descriere 4", "Titlu ceva acolo", "Nume4");
    }

    void PopulateServer()
    {
        // Get data from firebase or something...

    }



}
