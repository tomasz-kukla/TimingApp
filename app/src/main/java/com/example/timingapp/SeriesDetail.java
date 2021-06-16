package com.example.timingapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SeriesDetail {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("seasonList")
    private List<Season> seasonList = null;

    public SeriesDetail(String id, String name, List<Season> seasonList) {
        this.id = id;
        this.name = name;
        this.seasonList = seasonList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Season> getSeasonList() {
        return seasonList;
    }


}
