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

import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;

/**
 * Created by Mou on 27/9/15.
 */
public class MoviesNetworkTask extends AsyncTask<String, Void, List<Movie>> {

    private final String LOG_TAG = MoviesNetworkTask.class.getSimpleName();

    private Context mContext;
    private IMoviesNetworkListener mListener;

    public MoviesNetworkTask (Context context, IMoviesNetworkListener listener) {
        mContext  = context;
        mListener = listener;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonString = null;

        try {
            Uri uri = MovieUtils.getMoviesUri(mContext);
            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) { return null; }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) { buffer.append(line + "\n"); }

            if (buffer.length() == 0) { return null; }

            moviesJsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) { urlConnection.disconnect(); }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return parsingMoviesJson(moviesJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<Movie> parsingMoviesJson (String moviesJsonString) throws JSONException {
        final String RESULTS = "results";

        final String ID             = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String MOVIE_POSTER   = "poster_path";
        final String SYNOPSIS       = "overview";
        final String USER_RATING    = "vote_average";
        final String RELEASE_DATE   = "release_date";

        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(moviesJsonString);
        JSONArray moviesResult = moviesJson.getJSONArray(RESULTS);

        for(int i=0, length = moviesResult.length(); i < length; ++i) {
            JSONObject movieJson = moviesResult.getJSONObject(i);

            Movie movie = new Movie.Builder()
                    .setId(movieJson.getLong(ID))
                    .setOriginalTitle(movieJson.optString(ORIGINAL_TITLE))
                    .setMoviePoster(movieJson.optString(MOVIE_POSTER))
                    .setSynopsis(movieJson.optString(SYNOPSIS))
                    .setUserRating((float) movieJson.optDouble(USER_RATING))
                    .setReleaseDate(movieJson.optString(RELEASE_DATE))
                    .build();

            movies.add(movie);
        }

        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if(movies != null && mListener != null) {
            mListener.onMoviesLoaded(movies);
        }
    }

    public interface IMoviesNetworkListener {
        void onMoviesLoaded (List<Movie> movies);
    }
}
