package com.example.wallappwallpaper;

public class WallPaperValidator {

    public void validate(WallPaper wallPaper) throws Exception {
        String errorMessages = "";

        // Validation stuff
        if(wallPaper.getName().length() <= 0) {
            errorMessages += "Invalid Name\n";
        }

        ///

        if(errorMessages.length() > 0)
        {
            throw new Exception("Invalid wallpaper...");
        }

    }


}
