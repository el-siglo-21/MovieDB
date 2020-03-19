package com.example.moviedbapp.Networks;

import com.example.moviedbapp.Models.DetailMovie;
import com.example.moviedbapp.Models.Movies;
import com.example.moviedbapp.Models.Reviews;
import com.example.moviedbapp.Models.VideoResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesAPI {

    @GET("discover/movie")
    Call<Movies> getMovies(@Query("api_key") String api_key, @Query("page") int page, @Query("with_genres") String with_genres);
    @GET("movie/{movie_id}")
    Call<DetailMovie> getDetailMovie(@Path("movie_id") int movie_id, @Query("api_key") String api_key);
    @GET("movie/{movie_id}/reviews")
    Call<Reviews> getReviews(@Path("movie_id") int movie_id, @Query("api_key") String api_key, @Query("page") int page);
    @GET("movie/{movie_id}/videos")
    Call<VideoResults> getVideos(@Path("movie_id") int movie_id, @Query("api_key") String api_key);
}

