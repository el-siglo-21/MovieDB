package com.example.moviedbapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.moviedbapp.Adapters.ReviewsAdapter;
import com.example.moviedbapp.Models.DetailMovie;
import com.example.moviedbapp.Models.Review;
import com.example.moviedbapp.Models.Reviews;
import com.example.moviedbapp.Networks.MoviesAPI;
import com.example.moviedbapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailMovieActivity extends AppCompatActivity {
    private int movie_id, total_pages, page;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    ArrayList<Review> mReviews;
    RecyclerView mRVReviews;
    ReviewsAdapter mReviewsAdapter;
    Button mBtnTrailer;
    ImageView mIVPicture;
    TextView mTVDetailTitle, mTVRating, mTVRuntime, mTVDetailOverview, mTVReviewTitle;
    DetailMovie mDetailMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        mDetailMovie = new DetailMovie();
        mIVPicture = (ImageView)findViewById(R.id.IVPicture);
        mTVDetailTitle = (TextView)findViewById(R.id.TVDetailTitle);
        mTVRating = (TextView)findViewById(R.id.TVRating);
        mTVRuntime = (TextView)findViewById(R.id.TVRuntime);
        mTVDetailOverview = (TextView)findViewById(R.id.TVDetailOverview);
        mBtnTrailer = (Button)findViewById(R.id.btnTrailer);
        mTVReviewTitle = (TextView)findViewById(R.id.TVReviewTitle);

        mReviews = new ArrayList<>();
        mRVReviews = (RecyclerView)findViewById(R.id.RVReviews);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRVReviews.setLayoutManager(layoutManager);
        mReviewsAdapter = new ReviewsAdapter(getApplicationContext(), mReviews);
        mRVReviews.setAdapter(mReviewsAdapter);

        Intent intent = getIntent();
        movie_id = intent.getIntExtra("movie_id", 0);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);

        Call<DetailMovie> call = moviesAPI.getDetailMovie(movie_id, "668db373e36ebd9085d4685d02318231");
        call.enqueue(new Callback<DetailMovie>() {
            @Override
            public void onResponse(Call<DetailMovie> call, Response<DetailMovie> response) {
                if (response.isSuccessful()) {
                    mDetailMovie = response.body();
                    Glide.with(getApplicationContext())
                            .load("https://image.tmdb.org/t/p/w500" + mDetailMovie.getBackdrop_path())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(mIVPicture);
                    mTVDetailTitle.setText(mDetailMovie.getTitle());
                    mTVRuntime.setText(mDetailMovie.getRuntime() + " minutes");
                    mTVRating.setText("Rating: " + mDetailMovie.getVote_average() + "/10");
                    mTVDetailOverview.setText(mDetailMovie.getOverview());
                }
                else {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(DetailMovieActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(DetailMovieActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(DetailMovieActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailMovie> call, Throwable t) {
                Toast.makeText(DetailMovieActivity.this, "Network Connection Failured", Toast.LENGTH_LONG).show();
            }
        });

        page = 1;
        Call<Reviews> reviewsCall = moviesAPI.getReviews(movie_id, "668db373e36ebd9085d4685d02318231", page);
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                if (response.isSuccessful()) {
                    total_pages = response.body().getTotal_pages();
                    if (response.body().getResults().size() == 0) {
                        mTVReviewTitle.setVisibility(View.GONE);
                    } else {
                        mReviews.addAll(response.body().getResults());
                        mReviewsAdapter.setmReviews(mReviews);
                    }
                    page++;
                }
                else {
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(DetailMovieActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(DetailMovieActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(DetailMovieActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Toast.makeText(DetailMovieActivity.this, "Network Connection Failured", Toast.LENGTH_LONG).show();
            }
        });

        mRVReviews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            //Do pagination
                            if (page < total_pages) {
                                Call<Reviews> reviewsCall = moviesAPI.getReviews(movie_id, "668db373e36ebd9085d4685d02318231", page);
                                reviewsCall.enqueue(new Callback<Reviews>() {
                                    @Override
                                    public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                                        if (response.isSuccessful()) {
                                            total_pages = response.body().getTotal_pages();
                                            mReviews.addAll(response.body().getResults());
                                            mReviewsAdapter.setmReviews(mReviews);
                                            page++;
                                        }
                                        else {
                                            switch (response.code()) {
                                                case 404:
                                                    Toast.makeText(DetailMovieActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case 500:
                                                    Toast.makeText(DetailMovieActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
                                                    break;
                                                default:
                                                    Toast.makeText(DetailMovieActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Reviews> call, Throwable t) {
                                        Toast.makeText(DetailMovieActivity.this, "Network Connection Failured", Toast.LENGTH_LONG).show();
                                    }
                                });
                                loading = true;
                            }
                        }
                    }
                }
            }
        });
        mBtnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TrailerActivity.class);
                intent.putExtra("movie_id", movie_id);
                startActivity(intent);
            }
        });
    }
}

