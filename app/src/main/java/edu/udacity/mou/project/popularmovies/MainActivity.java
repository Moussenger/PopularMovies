package edu.udacity.mou.project.popularmovies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.udacity.mou.project.popularmovies.model.Movie;


public class MainActivity extends AppCompatActivity implements MoviesFragment.IMovieClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String MOVIE_FRAGMENT_TAG = "Movie fragment";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoviesFragment fragment = (MoviesFragment) getFragmentManager().findFragmentById(R.id.fragment_movies);

        if(fragment != null) {
            fragment.setListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTwoPane = findViewById(R.id.fragment_movie_detail) != null;
    }

    @Override
    public void onMovieClick(Movie movie, boolean forcedByReload) {
        if(mTwoPane) {
            loadOnLeftPane(movie);
        } else {
            if(!forcedByReload) {
                openDetailActivity(movie);
            }
        }

    }

    private void openDetailActivity (Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MovieFragment.MOVIE, movie);

        startActivity(intent);
    }

    private void loadOnLeftPane (Movie movie) {
        if(movie!= null) {
            MovieFragment movieFragment = new MovieFragment();
            movieFragment.setArguments(getMovieFragmentBundle(movie));

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_movie_detail, movieFragment, MOVIE_FRAGMENT_TAG)
                    .commit();
        } else {
            Fragment fragment = getFragmentManager().findFragmentByTag(MOVIE_FRAGMENT_TAG);

            if(fragment != null) {
                getFragmentManager()
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }
    }

    private Bundle getMovieFragmentBundle (Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MovieFragment.MOVIE, movie);

        return bundle;
    }
}
