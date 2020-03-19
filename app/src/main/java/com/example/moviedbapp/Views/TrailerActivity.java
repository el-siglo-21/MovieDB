package com.example.moviedbapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.moviedbapp.MainActivity;
import com.example.moviedbapp.Models.VideoResults;
import com.example.moviedbapp.Networks.MoviesAPI;
import com.example.moviedbapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class TrailerActivity extends YouTubeBaseActivity {
    private int movie_id;
    private String youtube_key;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        youTubePlayerView = findViewById(R.id.youtubePlayerView);

        Intent intent = getIntent();
        movie_id = intent.getIntExtra("movie_id", 0);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);

        Call<VideoResults> call = moviesAPI.getVideos(movie_id, "668db373e36ebd9085d4685d02318231");
        call.enqueue(new Callback<VideoResults>() {
            @Override
            public void onResponse(Call<VideoResults> call, Response<VideoResults> response) {
                if (response.isSuccessful()) {
                    youtube_key = response.body().getResults().get(0).getKey();
                    youTubePlayerView.initialize("AIzaSyDIhjE15MPKMAg6RZE8kV_YTAK61XfDoWc", new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            youTubePlayer.cueVideo(youtube_key);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                            Toast.makeText(TrailerActivity.this, "Retry Load the Video", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(TrailerActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(TrailerActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(TrailerActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResults> call, Throwable t) {
                Toast.makeText(TrailerActivity.this, "Network Connection Failured", Toast.LENGTH_LONG).show();
            }
        });
    }
}

