package com.example.vuthyra.moviefavorite.Utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vuthyra.moviefavorite.R;
import com.example.vuthyra.moviefavorite.model.Video;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private final List<Video> mTrailerList;

    private Context mContext;

    public TrailerAdapter (List<Video> trailers, Context context) {

        this.mTrailerList = trailers;
        this.mContext = context;

    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View trailerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_trailer_layout, parent, false );
        TrailerHolder trailerHolder = new TrailerHolder(trailerView);
        return trailerHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerHolder holder, int position) {

        Video populatedVideo = mTrailerList.get(holder.getAdapterPosition());

        holder.mTrailerName.setText(populatedVideo.getmNameOfVideo());
        holder.mTrailerSite.setText(populatedVideo.getmSite());
        holder.mTrailerQualilty.setText(String.valueOf(populatedVideo.getmVideoQuality()));

        Log.d(TAG, "There are number of items " + getItemCount());

    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    /**
     * Indepentdent class of TrailerHolder that acts as a ViewHolder for our
     * RecyclerViewAdapter.
     */


    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mYoutubeLogo;
        private final TextView mTrailerName;
        private final TextView mTrailerSite;
        private final TextView mTrailerQualilty;


        TrailerHolder(View itemView) {

            super(itemView);

            mYoutubeLogo = (ImageView) itemView.findViewById(R.id.youtubeIcon);
            mTrailerName = (TextView) itemView.findViewById(R.id.trailer_name);
            mTrailerSite = (TextView) itemView.findViewById(R.id.trailer_site);
            mTrailerQualilty = (TextView) (TextView) itemView.findViewById(R.id.trailer_quality);
            mYoutubeLogo.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            //Getting access to current video .
            Video videoCurrent = mTrailerList.get(position);

            //Getting the youtube key to put the end point of youtube Url.
            String key = videoCurrent.getmYoutubeKey();

            String url = "https://www.youtube.com/watch?v=" + key;
            Intent i = new Intent(Intent.ACTION_VIEW);

            //Checking to make sure that this intent would result into a proper activity.
            PackageManager packageManager = mContext.getPackageManager();
            if (i.resolveActivity(packageManager) != null) {
                i.setData(Uri.parse(url));
                mContext.startActivity(i);
            }

        }

    }


}