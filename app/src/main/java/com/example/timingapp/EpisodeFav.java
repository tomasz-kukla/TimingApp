package com.example.timingapp;

public class EpisodeFav {

    private boolean onWatchedList = false;

    public EpisodeFav(boolean onWatchedList) {
        this.onWatchedList = onWatchedList;
    }

    public boolean isOnWatchedList() {
        return onWatchedList;
    }
}
