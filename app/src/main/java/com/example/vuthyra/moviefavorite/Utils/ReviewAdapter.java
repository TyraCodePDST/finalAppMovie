package com.example.vuthyra.moviefavorite.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vuthyra.moviefavorite.R;
import com.example.vuthyra.moviefavorite.model.Review;

import java.util.List;

public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

        private final List<Review> mReviewList;

        private Context mContext;

        public ReviewAdapter (List<Review> reviews, Context context) {
            this.mReviewList = reviews;
            this.mContext = context;
        }

        @NonNull
        @Override
        public ReviewAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View reviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_review_layout, parent, false );
            ReviewHolder reviewHolder = new ReviewHolder(reviewView);
            return reviewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull ReviewAdapter.ReviewHolder holder, int position) {

            Review bindReview = mReviewList.get(holder.getAdapterPosition());
            holder.mAuthorName.setText(bindReview.getmAuthor());
            holder.mDetailReview.setText(bindReview.getmContent());
            holder.mUrl.setText(bindReview.getmUrl());
        }

        @Override
        public int getItemCount() {
            return mReviewList.size();
        }

        /**
         * Indepentdent class of TrailerHolder that acts as a ViewHolder for our
         * RecyclerViewAdapter.
         */


        public class ReviewHolder extends RecyclerView.ViewHolder {

            private final TextView mAuthorName;
            private final TextView mDetailReview;
            private final TextView mUrl;

            ReviewHolder (View itemView) {

                super(itemView);
                mAuthorName = (TextView) itemView.findViewById(R.id.author);
                mDetailReview = (TextView) itemView.findViewById(R.id.content);
                mUrl = (TextView) itemView.findViewById(R.id.url);

            }
        }
    }



