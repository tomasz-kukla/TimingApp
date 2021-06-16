package com.example.timingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EpisodeList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("noOfEpisode")
    @Expose
    private int noOfEpisode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNoOfEpisode() {
        return noOfEpisode;
    }

    public void setNoOfEpisode(int noOfEpisode) {
        this.noOfEpisode = noOfEpisode;
    }
}
