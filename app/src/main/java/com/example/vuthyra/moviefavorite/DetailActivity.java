package com.example.vuthyra.moviefavorite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vuthyra.moviefavorite.Utils.ReviewAdapter;
import com.example.vuthyra.moviefavorite.Utils.TrailerAdapter;
import com.example.vuthyra.moviefavorite.data.AddMovieViewModel;
import com.example.vuthyra.moviefavorite.data.AddMovieViewModelFactory;
import com.example.vuthyra.moviefavorite.data.AppDatabase;
import com.example.vuthyra.moviefavorite.data.AppExecutors;
import com.example.vuthyra.moviefavorite.model.Movie;
import com.example.vuthyra.moviefavorite.model.Review;
import com.example.vuthyra.moviefavorite.model.Video;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_id_tv)
    TextView movie_id;
    @BindView(R.id.title_tv)
    TextView movie_title;
    @BindView(R.id.thumbnail_image_header)
    ImageView poster;
    @BindView(R.id.rating)
    RatingBar movie_rating;
    @BindView(R.id.release_date)
    TextView movie_release_date;
    @BindView(R.id.plot_tv)
    TextView movie_plot;

    private static final String TAG = DetailActivity.class.getSimpleName();
    //Getting a current Api key from BuildConfig class.
    private static final String mApiKey = BuildConfig.MY_MOVIE_DB_API_KEY;
    //Instance from other activities.
    public static final String INSTANCE_MOVIE = "Movie";
    public static final String INSTANCE_MOVIE_FAVORITE = "instanceFavorite";
    private int mMovieId;
    private AppDatabase mDatabase;


    //Get a RecyclerView class to populate all the trailer videos for this movie{id} .
    @BindView(R.id.trailer_rv_container)
    RecyclerView mRecyclerViewTrailer;
    // A adapter to populate movie trailers and pass data along to view video trailer on web.
    private TrailerAdapter mAdapterTrailer;
    // An arraylist contain all the information about Video objects.
    private List<Video> trailerList;
    //Trailers Base Url.
    private static final String TRAILERS_URL = "http://api.themoviedb.org/3/movie/";


    //Get a RecyclerView class to populate all reviews for this movie{id}
    @BindView(R.id.review_rv_container)
    RecyclerView mRecyclerViewReview;

    private ReviewAdapter mAdapterReview;

    private List<Review> reviewList;

    //Reviews Base Url.
    private static final String REVIEWS_URL = "http://api.themoviedb.org/3/movie/";

    //Poster Url for DetailActivity.
    private static final String POSTER_URL = "http://image.tmdb.org/t/p/w780";


    //Get a Movie object to use inside this DetailActivity.class
    private static Movie movie = null;

    //Create private variable to hold int value of favorite movie id.
    private int favoriteId;

    private ToggleButton mToggleFavButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ButterKnife.bind(this);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(INSTANCE_MOVIE);
        if (movie == null) {
            Log.e(TAG, "Error passing intent value from the MainActivity.class");
        } else {

            findViewById(R.id.collapsing_toolbar);

            favoriteId= movie.getmId();
            movie_id.setText(String.valueOf(movie.getmId()));
            movie_title.setText(movie.getmTitle());
            Picasso.with(this).load(POSTER_URL + movie.getmPosterImage())
            .resize(800, 800)
                    .error(R.drawable.error)
                    .into(poster);
            movie_rating.setRating(movie.getmRating() / 2);
            movie_release_date.setText(movie.getmReleaseDate());
            movie_plot.setText(movie.getmPlotInfo());

        }

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MOVIE_FAVORITE)) {
            mMovieId = savedInstanceState.getInt(INSTANCE_MOVIE_FAVORITE, favoriteId);
        }

        //All the section to retrieve trailers for this specific movie.

        trailerList = new ArrayList<>();
        mAdapterTrailer = new TrailerAdapter(trailerList, this);
        loadUpTrailers();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewTrailer.setHasFixedSize(true);
        mRecyclerViewTrailer.setLayoutManager(layoutManager);
        mRecyclerViewTrailer.setNestedScrollingEnabled(false);

        //All of parts to retrieve reviews for this specific movie.

        reviewList = new ArrayList<>();
        mAdapterReview = new ReviewAdapter(reviewList, this);
        loadUpReviews();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewReview.setHasFixedSize(true);
        mRecyclerViewReview.setLayoutManager(layoutManager2);
        mRecyclerViewReview.setNestedScrollingEnabled(false);

        mDatabase = AppDatabase.getInstance(getApplicationContext());
        /**
         * Event when toggleButton is selected.
         */

        mToggleFavButton = findViewById(R.id.toggleButton);
        mToggleFavButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                if (mToggleFavButton.isChecked()) {

                    Snackbar.make(v, R.string.snackbar_added_favorite, Snackbar.LENGTH_SHORT).show();
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                                mDatabase.movieDao().insertFavorite(movie);
                        }
                    });
                }

                else {

                    Snackbar.make(v, R.string.snackbar_deleted_favorite, Snackbar.LENGTH_SHORT).show();
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDatabase.movieDao().deleteFavorite(movie);

                        }
                    });
                }
            }
        });// End of View.OnClickListener().

        loadTheIntent();

    } //End of onCreate.


    @Override
    protected void onSaveInstanceState ( final Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(INSTANCE_MOVIE_FAVORITE, favoriteId);

    }


    private void loadTheIntent(){

        final Intent intent = getIntent();
        if (intent.hasExtra(INSTANCE_MOVIE_FAVORITE)) {
            mMovieId = intent.getIntExtra(INSTANCE_MOVIE_FAVORITE, favoriteId);
            AddMovieViewModelFactory factory = new AddMovieViewModelFactory(mDatabase, mMovieId);
            final AddMovieViewModel viewModel = ViewModelProviders.of( DetailActivity.this, factory)
                                    .get(AddMovieViewModel.class);
            viewModel.getFavoriteMovie().observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie movie) {
                    populateUI(movie);
                }
            });
        }
    }//End of loadTheIntent method.




    /**
     * populateUI method inside this detail activity.
     */

    public void populateUI(Movie favMovie) {

        if (favMovie == null) {
            mToggleFavButton.setChecked(false);

        } else {
            mToggleFavButton.setChecked(true);
        }
    }



        /**
         * A helper method to combined a correct actual Url to parse
         * all the movie trailer regarding a specific movie {id}.
         */

        private String getTrailersUrl () {

            String trailersUrl = TRAILERS_URL + movie.getmId() + "/videos?api_key=" + mApiKey;
            return trailersUrl;
        }

    /**
     * A helper method to combined with a correct Url to parse to
     * all movie reviews regarding a specific movie {id}.
     */

        private String getReviewsUrl ()
        {
            String reviewsUrl = REVIEWS_URL + movie.getmId() + "/reviews?api_key=" + mApiKey;
            return reviewsUrl;
        }




    /**
     * A method for loading and retrieving information of movie Trailers.
     */

    private void loadUpTrailers () {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getTrailersUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //Parse JsonArray from the main.
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray arrayFromResult = jsonObject.getJSONArray("results");

                    for (int i = 0; i < arrayFromResult.length(); i++) {
                        JSONObject jo = arrayFromResult.getJSONObject(i);
                        //Attach each of the JSON object into Movie class with
                        //new attributes.
                        final Video parsedVideoFromJson = new Video(
                                jo.getString("key"),
                                jo.getString("name"),
                                jo.getString("site"),
                                jo.getInt("size"));

                        trailerList.add(parsedVideoFromJson);
                        mAdapterTrailer = new TrailerAdapter(trailerList, getApplicationContext());
                        mRecyclerViewTrailer.setAdapter(mAdapterTrailer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, R.string.error_no_json_data, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }//End of loadUpTrailers.


    private void loadUpReviews () {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getReviewsUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //Parse JsonArray from the main.
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray arrayFromResult = jsonObject.getJSONArray("results");

                    for (int i = 0; i < arrayFromResult.length(); i++) {
                        JSONObject jo = arrayFromResult.getJSONObject(i);
                        //Attach each of the JSON object into Movie class with
                        //new attributes.
                        final Review parsedReviewsFromJson = new Review(
                                jo.getString("author"),
                                jo.getString("content"),
                                jo.getString("url"));

                        reviewList.add(parsedReviewsFromJson);
                        mAdapterReview = new ReviewAdapter(reviewList, getApplicationContext());
                        mRecyclerViewReview.setAdapter(mAdapterReview);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, R.string.error_no_json_data, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }











}








