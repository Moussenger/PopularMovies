package edu.udacity.mou.project.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
 * Created by Mou on 26/9/15.
 */
public class MovieFragment extends Fragment {

    public static final String MOVIE = "movie";

    private TextView  mTitleTextView;
    private ImageView mPosterImageView;
    private TextView  mDateTextView;
    private TextView  mRatingTextView;
    private TextView  mSynopsisTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, true);

        populateViews(view);

        Intent intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra(MOVIE)) {
            Movie movie = intent.getParcelableExtra(MOVIE);
            populateData(movie);
        }

        return view;
    }

    private void populateViews (View view) {
        mTitleTextView    = (TextView) view.findViewById(R.id.movie_title);
        mPosterImageView  = (ImageView) view.findViewById(R.id.movie_image);
        mDateTextView     = (TextView) view.findViewById(R.id.movie_date);
        mRatingTextView   = (TextView) view.findViewById(R.id.movie_rating);
        mSynopsisTextView = (TextView) view.findViewById(R.id.movie_synopsis);
    }

    private void populateData(Movie movie) {
        mTitleTextView.setText(movie.getOriginalTitle());
        mDateTextView.setText(MovieUtils.getYearOfDate(movie.getReleaseDate()));
        mRatingTextView.setText(getString(R.string.format_rating, movie.getUserRating()));
        mSynopsisTextView.setText(movie.getSynopsis());

        Picasso.with(getActivity())
                .load(MovieUtils.getImageUri(getActivity(), movie.getMoviePoster()))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_placeholder)
                .fit()
                .centerInside()
                .into(mPosterImageView);
    }
}
