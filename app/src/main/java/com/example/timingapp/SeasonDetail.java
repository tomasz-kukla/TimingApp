package com.example.timingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SeasonDetail {

    @SerializedName("id")
    private String id;
    @SerializedName("noOfSeason")
    private int noOfSeason;
    @SerializedName("episodes")
    private List<EpisodeList> episodes = null;

    public SeasonDetail(String id, int noOfSeason, List<EpisodeList> episodes) {
        this.id = id;
        this.noOfSeason = noOfSeason;
        this.episodes = episodes;
    }

    public String getId() {
        return id;
    }

    public int getNoOfSeason() {
        return noOfSeason;
    }

    public List<EpisodeList> getEpisodes() {
        return episodes;
    }
}
