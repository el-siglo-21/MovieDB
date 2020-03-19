package com.example.moviedbapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Genres {
    @SerializedName("genres")
    public List<Genre> Genres;

    public Genres(List<Genre> genres) {
        Genres = genres;
    }

    public List<Genre> getGenres() {
        return Genres;
    }

    public void setGenres(List<Genre> genres) {
        Genres = genres;
    }
}
