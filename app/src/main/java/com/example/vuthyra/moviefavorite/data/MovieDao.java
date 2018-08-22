package com.example.vuthyra.moviefavorite.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.vuthyra.moviefavorite.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favorite_table ORDER BY mTitle ASC")
    LiveData<List<Movie>> loadAllMovies();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(Movie movieEntry);

    //I commented this out because there's no need to use this method in
    //my current app. Only needed if the app makes user changes the information
    //about each movie.
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateFavorite(Movie movieEntry);

    @Delete
    void deleteFavorite(Movie movieEntry);

    @Query("SELECT * FROM favorite_table WHERE mid = :id")
    LiveData<Movie> loadFavoriteById(int id);


}
