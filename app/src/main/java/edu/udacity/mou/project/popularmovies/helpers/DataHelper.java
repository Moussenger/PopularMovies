package edu.udacity.mou.project.popularmovies.helpers;

import android.content.Context;

import edu.udacity.mou.project.popularmovies.data.operations.MovieProviderOperations;
import edu.udacity.mou.project.popularmovies.data.operations.impl.MovieProviderOperationsImpl;

/**
 * Created by mou on 19/06/16.
 */
public class DataHelper {

    public static MovieProviderOperations getMovieProviderOperations (Context context) {
        return new MovieProviderOperationsImpl(context);
    }
}
