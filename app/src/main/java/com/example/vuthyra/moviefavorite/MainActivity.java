package com.example.vuthyra.moviefavorite;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vuthyra.moviefavorite.Utils.MovieAdapter;
import com.example.vuthyra.moviefavorite.model.Movie;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Please put your own api code here.
    private static final String mApi_key = BuildConfig.MY_MOVIE_DB_API_KEY;

    private static final String BASE_URL_MOST_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=" + mApi_key;

    private static final String BASE_URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + mApi_key;

    // A recycler view to populate data in grid layout.
    private RecyclerView mRecyclerView;
    // Progress bar to show the state of loading pictures into recycler view container.
    private ProgressBar mProgressBar;

    //int value to store for selection from MenuItem by user.

    public final static String INSTANCE_MENU_ID = "instanceMenu";
    public final static String MENU_FAVORITE_SELECTED ="favorite";

    private String selected = "selection";

    private GridLayoutManager layoutManager;
    // A main adapter to populate movie details and pass data along to the new activity.
    private MovieAdapter mAdapter;
    // An arraylist contain all the information about Movie objects.
    private List<Movie> movieList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);

        mRecyclerView = findViewById(R.id.rv_container);
        mProgressBar = findViewById(R.id.pb_loading_indicator);

        movieList = new ArrayList<>();
        mAdapter = new MovieAdapter(this, movieList);
        int numOfColumns = mAdapter.getColumns(getApplicationContext());
        layoutManager = new GridLayoutManager(this, numOfColumns);

        //Called savedInstanceState from Bundle in case there's a
        //configuration changes prior to this.
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MENU_ID)) {
            selected = savedInstanceState.getString(INSTANCE_MENU_ID);
            if (selected != null) {
                loadUpdatedMovies(selected);
            }
            if (selected == MENU_FAVORITE_SELECTED ) {
                Intent intent = new Intent(this, FavoriteActivity.class);
                PackageManager packageManager = this.getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    this.startActivity(intent);
                }
            }
        }

        //Call onto this method, to invoke and generate data from JSON.
        loadUpdatedMovies(BASE_URL_MOST_POPULAR);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //Save an instance mIdMenu selection from the user.
        savedInstanceState.putString(INSTANCE_MENU_ID, selected);

    }


    /**
     * Method to parse Json data from the Moviedb.org api.
     */

    public void loadUpdatedMovies(String baseUrl) {

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setMax(120);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                putApiKey(baseUrl), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Parse JsonObjects from the main query.
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        //Attach each of the JSON object into Movie class with
                        //new attributes.
                        final Movie parsedMovieFromJson = new Movie(
                                jo.getInt("id"),
                                jo.getString("title"),
                                jo.getString("poster_path"),
                                jo.getString("overview"),
                                jo.getString("release_date"),
                                Float.parseFloat(jo.getString("vote_average")));

                        movieList.add(parsedMovieFromJson);
                        mAdapter = new MovieAdapter(getApplicationContext(), movieList);
                        mRecyclerView.setAdapter(mAdapter);
                        mProgressBar.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,R.string.error_no_json_data, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    /**
     * Method , that checks whether user has correctly put the API keys,
     * provided warning, otherwise.
     */

    public String putApiKey(String check) {

        String checked = "";
        if (check.contains(mApi_key) && mApi_key != "" && !mApi_key.isEmpty()) {
            checked = check;
        } else {
            Toast.makeText(this, R.string.error_no_api_key, Toast.LENGTH_LONG).show();
            checked = "";
        }
        return checked;
    }


    /**
     * Menu selections, sort movie based on popularity or highest rating
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.sort_order, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Item) {

        //This method is a little Strange but i got it to work as expected.

        int id = Item.getItemId();

        switch (id) {

            case R.id.action_popular:
                movieList.clear();
                mAdapter.notifyDataSetChanged();
                loadUpdatedMovies(BASE_URL_MOST_POPULAR);
                selected = BASE_URL_MOST_POPULAR;
                return true;


            case R.id.action_highest_rated:
                movieList.clear();
                mAdapter.notifyDataSetChanged();
                loadUpdatedMovies(BASE_URL_TOP_RATED);
                selected = BASE_URL_TOP_RATED;
                return true;


            case R.id.action_favorite_lists:
                movieList.clear();
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(this, FavoriteActivity.class);
                PackageManager packageManager = this.getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    this.startActivity(intent);
                }
                selected = MENU_FAVORITE_SELECTED;
                return true;

        }
        return super.onOptionsItemSelected(Item);

    }


}