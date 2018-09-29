package com.example.user.trendy.Category.ProductDetail.Filter;

import java.util.ArrayList;

public class MainFilterModel {

    public static int TAG = 1;
    public static int TYPE = 2;
    public static int VENDOR = 3;

    public static int INDEX_TAG = 0;
    public static int INDEX_TYPE = 1;
    public static int INDEX_VENDOR = 2;


    String title, sub;
    boolean isSelected;
    ArrayList<String> subtitles = new ArrayList<String>();

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public ArrayList<String> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(ArrayList<String> subtitles) {
        this.subtitles = subtitles;

    }
}
