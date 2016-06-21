package edu.udacity.mou.project.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.udacity.mou.project.popularmovies.data.operations.MovieProviderOperations;
import edu.udacity.mou.project.popularmovies.helpers.DataHelper;
import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.network.AbstractGeneralMoviesNetworkTask;
import edu.udacity.mou.project.popularmovies.network.MoviesNetworkTask;
import edu.udacity.mou.project.popularmovies.network.TrailersNetworkTask;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;

/**
 * Created by Mou on 26/9/15.
 */
public class MovieFragment extends Fragment implements View.OnClickListener{
    public static final String LOG_TAG = MovieFragment.class.getSimpleName();
    public static final String MOVIE = "movie";

    private static final int STAR_EMPTY    = R.drawable.fav_empty_icon;
    private static final int STAR_SELECTED = R.drawable.fav_selected_icon;

    private TextView  mTitleTextView;
    private ImageView mPosterImageView;
    private TextView  mDateTextView;
    private TextView  mRatingTextView;
    private TextView  mSynopsisTextView;
    private ImageView mFavImageView;

    private Movie mMovie;

    private MovieProviderOperations mMovieProviderOperations;


    private AbstractGeneralMoviesNetworkTask.INetworkListener mTrailersListener = new AbstractGeneralMoviesNetworkTask.INetworkListener() {
        @Override
        public void onStartLoad() {

        }

        @Override
        public void onDataLoaded(List data) {
            Log.d(LOG_TAG, data.toString());
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, true);

        populateViews(view);

        Intent intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra(MOVIE)) {
            Movie movie = intent.getParcelableExtra(MOVIE);
            mMovie = movie;
            mMovieProviderOperations = DataHelper.getMovieProviderOperations(getActivity().getApplicationContext());
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
        mFavImageView     = (ImageView) view.findViewById(R.id.movie_fav);
    }

    private void populateData(Movie movie) {
        mTitleTextView.setText(movie.getOriginalTitle());
        mDateTextView.setText(MovieUtils.getYearOfDate(movie.getReleaseDate()));
        mRatingTextView.setText(getString(R.string.format_rating, movie.getUserRating()));
        mSynopsisTextView.setText(movie.getSynopsis());
        mFavImageView.setOnClickListener(this);

        mMovie.setFavorite(mMovieProviderOperations.isFavorite(mMovie));

        updateFavoriteIcon();

        Picasso.with(getActivity())
                .load(MovieUtils.getImageUri(getActivity(), movie.getMoviePoster()))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_placeholder)
                .fit()
                .centerInside()
                .into(mPosterImageView);
    }

    @Override
    public void onClick(View view) {
        boolean newValue = !mMovie.isFavorite();
        mMovie.setFavorite(newValue);
        updateFavoriteIcon();
        updateStoreFavorite(newValue);
    }

    @Override
    public void onStart() {
        super.onStart();

        TrailersNetworkTask networkTask = new TrailersNetworkTask(getActivity(), mMovie, mTrailersListener);
        networkTask.execute();
    }

    private void updateStoreFavorite (boolean favorite) {
        if(favorite) {
            mMovieProviderOperations.insertFavorite(mMovie);
        } else {
            mMovieProviderOperations.removeFavorite(mMovie);
        }
    }

    private void updateFavoriteIcon () {
        mFavImageView.setImageResource(mMovie.isFavorite() ? STAR_SELECTED : STAR_EMPTY);
    }
}
