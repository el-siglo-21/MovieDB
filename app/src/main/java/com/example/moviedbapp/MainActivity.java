package com.example.moviedbapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedbapp.Adapters.MainActivityAdapter;
import com.example.moviedbapp.Models.Genre;
import com.example.moviedbapp.Models.Genres;
import com.example.moviedbapp.Networks.GenresAPI;
import com.example.moviedbapp.Views.MoviesActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainActivityAdapter.OnGenreListener{
    List<Genre> genreList;
    RecyclerView recyclerView;
    MainActivityAdapter mainActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        genreList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.RVGenres);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mainActivityAdapter = new MainActivityAdapter(getApplicationContext(), genreList, this);
        recyclerView.setAdapter(mainActivityAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GenresAPI genresAPI = retrofit.create(GenresAPI.class);

        Call<Genres> call = genresAPI.getGenres("668db373e36ebd9085d4685d02318231", "en-US");
        call.enqueue(new Callback<Genres>() {
            @Override
            public void onResponse(Call<Genres> call, Response<Genres> response) {
                if(response.isSuccessful()) {
                    genreList.addAll(response.body().getGenres());
                    mainActivityAdapter.setGenreList(genreList);
                }
                else {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(MainActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(MainActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Connection Failured", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onGenreClick(int position) {
        Intent intent = new Intent(this, MoviesActivity.class);
        intent.putExtra("id", genreList.get(position).getId());
        startActivity(intent);
    }
}
