package com.example.moviedbapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedbapp.Models.Review;
import com.example.moviedbapp.R;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<Review> mReviews;

    public ReviewsAdapter(Context mContext, ArrayList<Review> mReviews) {
        this.mContext = mContext;
        this.mReviews = mReviews;
    }

    public void setmReviews(ArrayList<Review> mReviews) {
        this.mReviews = mReviews;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_review, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.mTVAuthor.setText("Author: " + mReviews.get(i).getAuthor());
        myViewHolder.mTVContent.setText(mReviews.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviews != null)
            return mReviews.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mTVAuthor, mTVContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTVAuthor = (TextView)itemView.findViewById(R.id.TVAuthor);
            mTVContent = (TextView)itemView.findViewById(R.id.TVContent);
        }
    }
}
