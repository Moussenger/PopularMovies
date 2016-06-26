package edu.udacity.mou.project.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.udacity.mou.project.popularmovies.adapters.ReviewsRecyclerViewAdapter;
import edu.udacity.mou.project.popularmovies.adapters.TrailersRecyclerViewAdapter;
import edu.udacity.mou.project.popularmovies.data.operations.MovieProviderOperations;
import edu.udacity.mou.project.popularmovies.helpers.DataHelper;
import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.model.Review;
import edu.udacity.mou.project.popularmovies.model.Trailer;
import edu.udacity.mou.project.popularmovies.network.AbstractGeneralMoviesNetworkTask;
import edu.udacity.mou.project.popularmovies.network.MoviesNetworkTask;
import edu.udacity.mou.project.popularmovies.network.ReviewsNetworkTask;
import edu.udacity.mou.project.popularmovies.network.TrailersNetworkTask;
import edu.udacity.mou.project.popularmovies.utils.IntentUtils;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;
import edu.udacity.mou.project.popularmovies.views.RecyclerViewWithEmptyView;

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
    private RecyclerViewWithEmptyView mTrailersRecyclerView;
    private TrailersRecyclerViewAdapter mTrailersRecyclerViewAdapter;
    private TextView mEmptyTrailersView;
    private RecyclerViewWithEmptyView mReviewsRecyclerView;
    private ReviewsRecyclerViewAdapter mReviewsRecyclerViewAdapter;
    private TextView mEmptyReviewsView;

    private ShareActionProvider mShareActionProvider;
    private MenuItem mShareTrailerMenuItem;

    private Movie mMovie;
    private Trailer mFirstTrailer;

    private MovieProviderOperations mMovieProviderOperations;

    private AbstractGeneralMoviesNetworkTask.INetworkListener mTrailersListener = new AbstractGeneralMoviesNetworkTask.INetworkListener<Trailer>() {
        @Override
        public void onStartLoad() {

        }

        @Override
        public void onDataLoaded(List<Trailer> data) {
            mTrailersRecyclerViewAdapter.swap(data);

            if(data != null && data.size() != 0) {
                mFirstTrailer = data.get(0);
                updateShareAction();
            }
        }
    };

    private AbstractGeneralMoviesNetworkTask.INetworkListener mReviewsListener = new AbstractGeneralMoviesNetworkTask.INetworkListener<Review>() {
        @Override
        public void onStartLoad() {

        }

        @Override
        public void onDataLoaded(List<Review> data) {
            mReviewsRecyclerViewAdapter.swap(data);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        populateViews(view);

        Intent intent = getActivity().getIntent();
        Bundle arguments = getArguments();

        Movie movie = null;

        if(arguments != null) {
            movie = arguments.getParcelable(MOVIE);
        } else if(intent != null && intent.hasExtra(MOVIE)){
            movie = intent.getParcelableExtra(MOVIE);
        }

        if(movie != null) {
            mMovie = movie;
            mMovieProviderOperations = DataHelper.getMovieProviderOperations(getActivity().getApplicationContext());
            populateData(movie);
        }

        return view;
    }

    private void populateViews (View view) {
        mTitleTextView        = (TextView) view.findViewById(R.id.movie_title);
        mPosterImageView      = (ImageView) view.findViewById(R.id.movie_image);
        mDateTextView         = (TextView) view.findViewById(R.id.movie_date);
        mRatingTextView       = (TextView) view.findViewById(R.id.movie_rating);
        mSynopsisTextView     = (TextView) view.findViewById(R.id.movie_synopsis);
        mFavImageView         = (ImageView) view.findViewById(R.id.movie_fav);
        mTrailersRecyclerView = (RecyclerViewWithEmptyView) view.findViewById(R.id.trailers_list);
        mEmptyTrailersView    = (TextView) view.findViewById(R.id.empty_trailers);
        mReviewsRecyclerView  = (RecyclerViewWithEmptyView) view.findViewById(R.id.reviews_list);
        mEmptyReviewsView     = (TextView) view.findViewById(R.id.empty_reviews);
    }

    private void populateData(Movie movie) {
        mTitleTextView.setText(movie.getOriginalTitle());
        mDateTextView.setText(MovieUtils.getYearOfDate(movie.getReleaseDate()));
        mRatingTextView.setText(getString(R.string.format_rating, movie.getUserRating()));
        mSynopsisTextView.setText(movie.getSynopsis());
        mFavImageView.setOnClickListener(this);

        final int trailerColumns = getResources().getInteger(R.integer.trailers_columns);
        mTrailersRecyclerView.setLayoutManager(new GridLayoutManager(mTrailersRecyclerView.getContext(), trailerColumns));
        mTrailersRecyclerView.setEmptyView(mEmptyTrailersView);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);

        mTrailersRecyclerViewAdapter = new TrailersRecyclerViewAdapter(getActivity().getApplicationContext());
        mTrailersRecyclerView.setAdapter(mTrailersRecyclerViewAdapter);

        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mReviewsRecyclerView.setEmptyView(mEmptyReviewsView);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);

        mReviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(getActivity().getApplicationContext());
        mReviewsRecyclerView.setAdapter(mReviewsRecyclerViewAdapter);

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

        TrailersNetworkTask trailersNetworkTask = new TrailersNetworkTask(getActivity(), mMovie, mTrailersListener);
        trailersNetworkTask.execute();

        ReviewsNetworkTask reviewsNetworkTask = new ReviewsNetworkTask(getActivity(), mMovie, mReviewsListener);
        reviewsNetworkTask.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie, menu);

        mShareTrailerMenuItem = menu.findItem(R.id.action_share_trailer);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShareTrailerMenuItem);

        mShareTrailerMenuItem.setVisible(false);
        updateShareAction();
    }

    private void updateShareAction () {
        if(mShareActionProvider != null && mFirstTrailer != null) {
            Context context = getActivity().getApplicationContext();
            Intent intent = IntentUtils.getTrailerShareIntent(context, mMovie, mFirstTrailer);
            mShareActionProvider.setShareIntent(intent);
            mShareTrailerMenuItem.setVisible(true);
        }
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
