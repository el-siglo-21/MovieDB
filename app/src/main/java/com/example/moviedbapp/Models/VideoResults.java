package com.example.moviedbapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideoResults {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private ArrayList<TrailerVideos> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<TrailerVideos> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerVideos> results) {
        this.results = results;
    }
}

