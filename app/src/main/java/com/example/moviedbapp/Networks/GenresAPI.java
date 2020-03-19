package com.example.moviedbapp.Networks;

import com.example.moviedbapp.Models.Genres;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenresAPI {

    @GET("genre/movie/list")
    Call<Genres> getGenres(@Query("api_key") String api_key, @Query("language") String language);
}
