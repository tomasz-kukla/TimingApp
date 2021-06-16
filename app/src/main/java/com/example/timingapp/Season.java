package com.example.timingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("noOfSeason")
    @Expose
    private int noOfSeason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNoOfSeason() {
        return noOfSeason;
    }

    public void setNoOfSeason(int noOfSeason) {
        this.noOfSeason = noOfSeason;
    }
}
