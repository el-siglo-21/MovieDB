package com.example.moviedbapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.moviedbapp.Models.Movie;
import com.example.moviedbapp.R;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<Movie> mMovies;
    OnMovieListener mOnMovieListener;

    public MoviesAdapter(Context mContext, ArrayList<Movie> mMovies, OnMovieListener onMovieListener) {
        this.mContext = mContext;
        this.mMovies = mMovies;
        this.mOnMovieListener = onMovieListener;
    }

    public void setmMovies(ArrayList<Movie> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_movie, viewGroup, false);
        return new MyViewHolder(view, mOnMovieListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.tvTitle.setText(mMovies.get(i).getTitle());
        myViewHolder.tvOverview.setText(mMovies.get(i).getOverview());
        Glide.with(mContext)
                .load("https://image.tmdb.org/t/p/w500" + mMovies.get(i).getPoster_path())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(myViewHolder.ivMovie);
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivMovie;
        TextView tvTitle, tvOverview;
        OnMovieListener onMovieListener;

        public MyViewHolder(View itemView, OnMovieListener onMovieListener) {
            super(itemView);
            ivMovie = (ImageView)itemView.findViewById(R.id.IVMovie);
            tvTitle = (TextView)itemView.findViewById(R.id.TVTitle);
            tvOverview = (TextView)itemView.findViewById(R.id.TVOverview);
            this.onMovieListener = onMovieListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMovieListener.onMovieClick(getAdapterPosition());
        }
    }

    public interface OnMovieListener {
        void onMovieClick(int position);
    }
}
