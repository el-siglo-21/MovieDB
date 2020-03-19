package com.example.moviedbapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedbapp.Adapters.MoviesAdapter;
import com.example.moviedbapp.Models.Movie;
import com.example.moviedbapp.Models.Movies;
import com.example.moviedbapp.Networks.MoviesAPI;
import com.example.moviedbapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.OnMovieListener{
    private int page, total_pages;
    private boolean loading = true;
    private ProgressBar spinner;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    ArrayList<Movie> mMovies;
    RecyclerView mRecyclerView;
    MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        spinner = (ProgressBar)findViewById(R.id.PBMovies);
        page = 1;

        spinner.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        final String genre_ids = Integer.toString(intent.getIntExtra("id", 0));

        mMovies = new ArrayList<>();
        mRecyclerView = (RecyclerView)findViewById(R.id.RVMovies);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new MoviesAdapter(getApplicationContext(), mMovies, this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);

        final Call<Movies> call = moviesAPI.getMovies("668db373e36ebd9085d4685d02318231", page, genre_ids);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful()){
                    total_pages = response.body().getTotal_pages();
                    mMovies.addAll(response.body().getResults());
                    mMoviesAdapter.setmMovies(mMovies);
                    spinner.setVisibility(View.GONE);
                    page++;
                }
                else {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(MoviesActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(MoviesActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(MoviesActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Toast.makeText(MoviesActivity.this, "Network Connection Failured", Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        spinner.setVisibility(View.VISIBLE);
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            //Do pagination
                            if (page < total_pages) {
                                Call<Movies> call = moviesAPI.getMovies("668db373e36ebd9085d4685d02318231", page, genre_ids);
                                call.enqueue(new Callback<Movies>() {
                                    @Override
                                    public void onResponse(Call<Movies> call, Response<Movies> response) {
                                        if (response.isSuccessful()){
                                            for(Movie e: response.body().getResults()){
                                                if(!mMovies.contains(e))
                                                    mMovies.add(e);
                                            }
                                            mMoviesAdapter.notifyDataSetChanged();
                                            spinner.setVisibility(View.GONE);
                                            page++;
                                        }
                                        else {
                                            switch (response.code()) {
                                                case 404:
                                                    Toast.makeText(MoviesActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case 500:
                                                    Toast.makeText(MoviesActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
                                                    break;
                                                default:
                                                    Toast.makeText(MoviesActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Movies> call, Throwable t) {
                                        Toast.makeText(MoviesActivity.this, "Network Connection Failured", Toast.LENGTH_LONG).show();
                                    }
                                });
                                loading = true;
                            }
                            spinner.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra("movie_id", mMovies.get(position).getId());
        startActivity(intent);
    }
}
