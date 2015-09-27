package edu.udacity.mou.project.popularmovies;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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

/**
 * Created by Mou on 26/9/15.
 */
public class MoviesFragment extends Fragment {

    private GridView mMoviesGridView;
    private MoviesAdapter mMovieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, true);

        mMoviesGridView = (GridView) view.findViewById(R.id.movies_grid);
        mMovieAdapter   = new MoviesAdapter(getActivity());

        mMoviesGridView.setAdapter(mMovieAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        (new FetchMoviesTask()).execute();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonString = null;

            try {
                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, "popularity.desc")
                        .appendQueryParameter(API_KEY, getString(R.string.api_key))
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) { buffer.append(line + "\n"); }

                if (buffer.length() == 0) { return null; }

                moviesJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
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
            JSONArray  moviesResult = moviesJson.getJSONArray(RESULTS);

            for(int i=0, length = moviesResult.length(); i < length; ++i) {
                JSONObject movieJson = moviesResult.getJSONObject(i);

                Log.d(LOG_TAG, movieJson.toString());

                Movie movie = new Movie.Builder()
                                .setId(movieJson.getLong(ID))
                                .setOriginalTitle(movieJson.optString(ORIGINAL_TITLE))
                                .setMoviePoster(movieJson.optString(MOVIE_POSTER))
                                .setSynopsis(moviesJson.optString(SYNOPSIS))
                                .setUserRating((float) movieJson.optDouble(USER_RATING))
                                .setReleaseDate(moviesJson.optString(RELEASE_DATE))
                                .build();

                movies.add(movie);
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            Log.d(LOG_TAG, movies.toString());

            if(movies != null) {
                mMovieAdapter.clear();
                mMovieAdapter.addAll(movies);
            }
        }
    }
}
