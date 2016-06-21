package edu.udacity.mou.project.popularmovies.data.operations.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.mou.project.popularmovies.data.MovieColumns;
import edu.udacity.mou.project.popularmovies.data.MovieProvider;
import edu.udacity.mou.project.popularmovies.data.operations.MovieProviderOperations;
import edu.udacity.mou.project.popularmovies.model.Movie;

/**
 * Created by mou on 19/06/16.
 */
public class MovieProviderOperationsImpl implements MovieProviderOperations {
    private static final String LOG_TAG = MovieProviderOperationsImpl.class.getName();

    private Context mContext;


    public MovieProviderOperationsImpl (Context context) {
        mContext = context;
    }

    @Override
    public void insertFavorite(Movie movie) {
        Uri insertUri = MovieProvider.Favorites.CONTENT_URI;

        try {
            mContext.getContentResolver().insert(insertUri, getMovieContentValues(movie));
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error deleting favorite movie: " + movie.toString(), e);
        }
    }

    @Override
    public void removeFavorite(Movie movie) {
        Uri removeUri = MovieProvider.Favorites.withId(movie.getId());

        try {
            mContext.getContentResolver().delete(removeUri, null, null);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error deleting favorite with contentUri: " + removeUri.toString(), e);
        }

    }

    @Override
    public List<Movie> getFavorites() {
        Cursor cursor = mContext.getContentResolver().query(MovieProvider.Favorites.CONTENT_URI, null, null, null, null);

        List<Movie> movies = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                movies.add(getMovieFromCursor(cursor));
            } while(cursor.moveToNext());
        }

        return movies;
    }

    @Override
    public boolean isFavorite(Movie movie) {
        Uri favoriteUri = MovieProvider.Favorites.withId(movie.getId());

        Cursor cursor = mContext.getContentResolver().query(favoriteUri, null, null, null, null);

        return cursor.moveToFirst();
    }

    private ContentValues getMovieContentValues (Movie movie) {
        ContentValues values = new ContentValues();

        values.put(MovieColumns._ID, movie.getId());
        values.put(MovieColumns.MOVIE_POSTER, movie.getMoviePoster());
        values.put(MovieColumns.ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieColumns.USER_RATING, movie.getUserRating());
        values.put(MovieColumns.SYNOPSIS, movie.getSynopsis());

        return values;
    }

    private Movie getMovieFromCursor (Cursor cursor) {
        return new Movie.Builder()
                .setId(cursor.getInt(cursor.getColumnIndex(MovieColumns._ID)))
                .setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieColumns.ORIGINAL_TITLE)))
                .setMoviePoster(cursor.getString(cursor.getColumnIndex(MovieColumns.MOVIE_POSTER)))
                .setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieColumns.RELEASE_DATE)))
                .setSynopsis(cursor.getString(cursor.getColumnIndex(MovieColumns.SYNOPSIS)))
                .setUserRating(cursor.getFloat(cursor.getColumnIndex(MovieColumns.USER_RATING)))
                .build();
    }
}
