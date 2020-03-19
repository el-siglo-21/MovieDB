package com.example.moviedbapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedbapp.Models.Genre;
import com.example.moviedbapp.R;

import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MyViewHolder> {
    Context context;
    List<Genre> genreList;
    OnGenreListener mOnGenreListener;

    public MainActivityAdapter(Context context, List<Genre> genreList, OnGenreListener onGenreListener) {
        this.context = context;
        this.genreList = genreList;
        this.mOnGenreListener = onGenreListener;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_genre, viewGroup, false);
        return new MyViewHolder(view, mOnGenreListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.tvGenre.setText(genreList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        if (genreList != null)
            return genreList.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvGenre;
        OnGenreListener onGenreListener;

        public MyViewHolder(View itemView, OnGenreListener onGenreListener) {
            super(itemView);
            tvGenre = (TextView)itemView.findViewById(R.id.name);
            this.onGenreListener = onGenreListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onGenreListener.onGenreClick(getAdapterPosition());
        }
    }

    public interface OnGenreListener{
        void onGenreClick(int position);
    }
}
