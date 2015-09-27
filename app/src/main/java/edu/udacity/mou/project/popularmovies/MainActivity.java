package edu.udacity.mou.project.popularmovies;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.udacity.mou.project.popularmovies.model.Movie;


public class MainActivity extends AppCompatActivity implements MoviesFragment.IMovieClickListener {

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
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MovieFragment.MOVIE, movie);

        startActivity(intent);
    }
}
