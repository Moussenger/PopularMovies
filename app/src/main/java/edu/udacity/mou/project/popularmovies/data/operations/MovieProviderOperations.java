package edu.udacity.mou.project.popularmovies.data.operations;

import java.util.List;

import edu.udacity.mou.project.popularmovies.model.Movie;

/**
 * Created by mou on 19/06/16.
 */
public interface MovieProviderOperations {
    void insertFavorite (Movie movie);

    void removeFavorite (Movie movie);

    List<Movie> getFavorites ();

    boolean isFavorite (Movie movie);
}
