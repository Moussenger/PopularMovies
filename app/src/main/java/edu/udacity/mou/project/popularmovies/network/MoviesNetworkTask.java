package edu.udacity.mou.project.popularmovies.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.udacity.mou.project.popularmovies.R;
import edu.udacity.mou.project.popularmovies.data.operations.MovieProviderOperations;
import edu.udacity.mou.project.popularmovies.helpers.DataHelper;
import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;

/**
 * Created by Mou on 27/9/15.
 */
public class MoviesNetworkTask extends AbstractGeneralMoviesNetworkTask<Movie> {
    private final String ID            = "id";
    private final String ORIGINAL_TITLE = "original_title";
    private final String MOVIE_POSTER   = "poster_path";
    private final String SYNOPSIS       = "overview";
    private final String USER_RATING    = "vote_average";
    private final String RELEASE_DATE   = "release_date";

    private INetworkListener mListener;
    private String mSortParam;

    public MoviesNetworkTask (Context context, INetworkListener listener) {
        super(context, listener);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mSortParam = MovieUtils.getSortParam(getContext());
    }

    @Override
    protected List<Movie> getDataFromCache() {
        if(mSortParam.equals(getContext().getString(R.string.favorites_sort))) {
            return DataHelper.getMovieProviderOperations(getContext()).getFavorites();
        }

        return null;
    }

    @Override
    protected Uri getUri() {
        return MovieUtils.getMoviesUri(getContext(), mSortParam);
    }

    @Override
    protected Movie parseObject(JSONObject object) throws JSONException {
        return new Movie.Builder()
                .setId(object.getLong(ID))
                .setOriginalTitle(object.optString(ORIGINAL_TITLE))
                .setMoviePoster(object.optString(MOVIE_POSTER))
                .setSynopsis(object.optString(SYNOPSIS))
                .setUserRating((float) object.optDouble(USER_RATING))
                .setReleaseDate(object.optString(RELEASE_DATE))
                .build();
    }
}
