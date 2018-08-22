package com.example.vuthyra.moviefavorite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.vuthyra.moviefavorite.Utils.MovieAdapter;
import com.example.vuthyra.moviefavorite.data.AppDatabase;
import com.example.vuthyra.moviefavorite.data.MovieViewModel;
import com.example.vuthyra.moviefavorite.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = FavoriteActivity.class.getSimpleName();



    // A recycler view to populate data in grid layout.
    private RecyclerView mRecyclerView;
    // Progress bar to show the state of loading pictures into recycler view container.
    private ProgressBar mProgressBar;

    // A main adapter to populate movie details and pass data along to the new activity.
    private MovieAdapter mAdapter;
    // An arraylist contain all the information about Movie objects.
    private List<Movie> movieList;
    // COMPLETED (1) Create AppDatabase member variable for the Database
    private AppDatabase mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_container);
        mProgressBar =  findViewById(R.id.pb_loading_indicator);
        movieList = new ArrayList<>();
        // COMPLETED (2) Initialize member variable for the data base
        mDatabase = AppDatabase.getInstance(getApplicationContext());
        loadFavorites();
        retrieveFavorites();

    }


    /**
     * Set a default back button pressed to go back to MainActivity.class
     *
     * @param
     * @return
     */

    public void onBackPressed() {
        Intent startMain = new Intent(FavoriteActivity.this, MainActivity.class);
        startActivity(startMain);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_highest_rated) {
            movieList.clear();
            mAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void loadFavorites() {

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(50);
        mAdapter = new MovieAdapter(this, movieList);
        int numOfColumns = mAdapter.getColumns(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, numOfColumns);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void retrieveFavorites(){

        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getFavoriteModel().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favoriteEntries) {
                Log.d(TAG, "Updating from LiveData object to ViewModel");
                mProgressBar.setVisibility(View.GONE);
                mAdapter.setFavoriteMovie(favoriteEntries);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onClick(View v) {
        //Using Intent along with Bundle to pass ArrayList object with the value of its position.
        Intent intent  = new Intent(this, DetailActivity.class);
        PackageManager packageManager = FavoriteActivity.this.getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            this.startActivity(intent);
        }
    }
}



