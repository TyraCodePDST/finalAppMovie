package com.example.vuthyra.moviefavorite.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.vuthyra.moviefavorite.model.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> favoriteModel;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mDataView = AppDatabase.getInstance(getApplication());
            favoriteModel = mDataView.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getFavoriteModel() {
        return favoriteModel;
    }
}
