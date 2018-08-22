package com.example.vuthyra.moviefavorite.data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class AddMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory{

        private final AppDatabase mDatabase;

        private final int mMovieId;

        public AddMovieViewModelFactory(AppDatabase database, int movieId) {
            mDatabase = database;
            mMovieId = movieId;
        }

        @Override

    public < T extends ViewModel > T create (Class<T> modelClass) {

            return (T)  new AddMovieViewModel(mDatabase, mMovieId);
        }

}
