package edu.udacity.mou.project.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.List;

import edu.udacity.mou.project.popularmovies.adapters.MoviesAdapter;
import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.network.MoviesNetworkTask;

/**
 * Created by Mou on 26/9/15.
 */
public class MoviesFragment extends Fragment implements MoviesNetworkTask.INetworkListener<Movie> {

    private GridView mMoviesGridView;
    private MoviesAdapter mMovieAdapter;
    private ProgressBar mLoadingMoviesProgress;

    private IMovieClickListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, true);

        mMoviesGridView        = (GridView) view.findViewById(R.id.movies_grid);
        mMovieAdapter          = new MoviesAdapter(getActivity());
        mLoadingMoviesProgress = (ProgressBar) view.findViewById(R.id.loading_movies_progress);

        mMoviesGridView.setEmptyView(view.findViewById(R.id.no_movies_view));
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
    public void onStartLoad() {
        mLoadingMoviesProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataLoaded(List<Movie> movies) {
        mLoadingMoviesProgress.setVisibility(View.GONE);
        mMovieAdapter.clear();

        if(movies!= null) {
            mMovieAdapter.addAll(movies);
        }
    }

    public interface IMovieClickListener {
        void onMovieClick (Movie movie);
    }
}
