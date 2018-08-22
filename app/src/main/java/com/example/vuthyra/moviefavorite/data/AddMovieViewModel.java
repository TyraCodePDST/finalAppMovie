package com.example.vuthyra.moviefavorite.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.vuthyra.moviefavorite.model.Movie;

public class AddMovieViewModel extends ViewModel{

    public static final String TAG = AddMovieViewModel.class.getSimpleName();


    private LiveData<Movie> favoriteMovie;

    public AddMovieViewModel(AppDatabase mData, int movieId) {

        favoriteMovie = mData.movieDao().loadFavoriteById(movieId);
        Log.d(TAG, "Called from AddMovieViewModel class.");
    }

    public LiveData<Movie>  getFavoriteMovie()  {

        return favoriteMovie;
    }

}
