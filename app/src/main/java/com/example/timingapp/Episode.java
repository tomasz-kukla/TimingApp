package com.example.timingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Episode {

    private String id;
    private String title;
    private String description;
    private int noOfEpisode;

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    public int getNoOfEpisode() {
        return noOfEpisode;
    }

}



