package com.example.timingapp;

import java.util.ArrayList;

public class Users_List {
    private String userId;
    private Boolean isOnWatchedList;
    private Series showDAO;

    public String getUserId() {
        return userId;
    }
    public Boolean getOnWatchedList() {
        return isOnWatchedList;
    }
    public Series getShowDAO() {
        return showDAO;
    }
}
