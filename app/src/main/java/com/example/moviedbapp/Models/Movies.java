package com.example.moviedbapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Movies {
    @SerializedName("page")
    public int page;
    @SerializedName("total_results")
    public int total_results;
    @SerializedName("total_pages")
    public int total_pages;
    @SerializedName("results")
    public ArrayList<Movie> results;

    public Movies(int page, int total_results, int total_pages, ArrayList<Movie> results) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}

