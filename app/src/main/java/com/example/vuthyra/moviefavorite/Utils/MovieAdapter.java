package com.example.vuthyra.moviefavorite.Utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vuthyra.moviefavorite.DetailActivity;
import com.example.vuthyra.moviefavorite.R;
import com.example.vuthyra.moviefavorite.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private static final String POSTER_URL = "http://image.tmdb.org/t/p/w500";

    private List<Movie> movieList;

    private Context mContext;

    public MovieAdapter(Context context, List<Movie> movies) {
        mContext = context;
        movieList = movies;
    }

    public int getColumns (Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }



    @NonNull
    @Override
    public MovieAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View movieView = inflater.inflate(R.layout.single_poster_layout, parent, false);
        MovieHolder movieHolder = new MovieHolder(movieView);
        Log.d(TAG, "created MovieHolder completed");

        return movieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieHolder holder, int position) {

        //Create new Movie object with the given value from List<Movie> movieList;
        Movie populateMovies = movieList.get(position);
        Picasso.with(mContext).load(POSTER_URL + populateMovies.getmPosterImage())
                .resize(1000, 1000)
                .error(R.drawable.error)
                .into(holder.movie_poster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * method to pass List<Movie> item to the next activity.
     */
    public List<Movie> returnMovie() {
        return movieList;
    }


    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setFavoriteMovie(List<Movie> movieEntries) {
        movieList = movieEntries;
    }

    /**
     * Indepentdent class of MovieHolder that acts as a ViewHolder for our
     * RecyclerViewAdapter.
     */

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private final ImageView movie_poster;
        MovieHolder(View itemView) {
            super(itemView);
            //Get a reference resource ID of ImageView of the movie poster,
            // then setOnClickListener to get response of what movie is selected.
            movie_poster = itemView.findViewById(R.id.single_poster);
            movie_poster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (returnMovie() == null) {
                Toast.makeText(mContext, R.string.null_onClick, Toast.LENGTH_LONG).show();
            }
            // Create a new List<Movie> to be passed into the DetailActivity.class
            // along with a position value, given by the getAdapterPosition() method.
            List<Movie> listMovies = new ArrayList<>(returnMovie());
            int position = getAdapterPosition();
            Movie mResultMovie = listMovies.get(position);

            //Using Intent to pass Movie object by a key name Movie.
            Intent intent = new Intent(mContext, DetailActivity.class);
            //Checking to make sure that this intent would result into a proper activity.
            PackageManager packageManager = mContext.getPackageManager();
            if (intent.resolveActivity(packageManager) != null) {
                intent.putExtra(DetailActivity.INSTANCE_MOVIE, mResultMovie);
                intent.putExtra(DetailActivity.INSTANCE_MOVIE_FAVORITE, mResultMovie.getmId());
                mContext.startActivity(intent);
                mContext.startActivity(intent);
            }
        }
    }
}