package edu.udacity.mou.project.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.Iterator;
import java.util.List;

import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.network.MoviesNetworkTask;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;

/**
 * Created by Mou on 26/9/15.
 */
public class MoviesFragment extends Fragment implements MoviesNetworkTask.IMoviesNetworkListener{

    private GridView mMoviesGridView;
    private MoviesAdapter mMovieAdapter;

    private IMovieClickListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, true);

        mMoviesGridView = (GridView) view.findViewById(R.id.movies_grid);
        mMovieAdapter   = new MoviesAdapter(getActivity());

        mMoviesGridView.setAdapter(mMovieAdapter);
        mMoviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onMovieClick(mMovieAdapter.getItem(position));
                }
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        MoviesNetworkTask networkTask = new MoviesNetworkTask(getActivity(), this);
        networkTask.execute();
    }

    public IMovieClickListener getListener() { return mListener; }
    public void setListener(IMovieClickListener listener) { mListener = listener; }

    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        mMovieAdapter.clear();
        mMovieAdapter.addAll(movies);
    }

    public interface IMovieClickListener {
        void onMovieClick (Movie movie);
    }
}
