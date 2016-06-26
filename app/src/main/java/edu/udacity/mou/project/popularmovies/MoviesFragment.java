package edu.udacity.mou.project.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.mou.project.popularmovies.adapters.MoviesRecyclerViewAdapter;
import edu.udacity.mou.project.popularmovies.adapters.TrailersRecyclerViewAdapter;
import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.network.MoviesNetworkTask;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;
import edu.udacity.mou.project.popularmovies.views.RecyclerViewWithEmptyView;

/**
 * Created by Mou on 26/9/15.
 */
public class MoviesFragment extends Fragment implements MoviesNetworkTask.INetworkListener<Movie> {
    private static final String SELECTED = "Selected";
    private static final String MOVIES = "Movies";

    private RecyclerViewWithEmptyView mMoviesRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private MoviesRecyclerViewAdapter mMovieAdapter;
    private ViewGroup mEmptyView;
    private ProgressBar mLoadingMoviesProgress;

    private ArrayList<Movie> mMovies;
    private int mSelected = -1;

    private IMovieClickListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, true);

        final boolean hasSelect = getResources().getBoolean(R.bool.hasSelect);
        mMoviesRecyclerView    = (RecyclerViewWithEmptyView) view.findViewById(R.id.movies_grid);
        mMovieAdapter          = new MoviesRecyclerViewAdapter(getActivity().getApplicationContext(), hasSelect);
        mLoadingMoviesProgress = (ProgressBar) view.findViewById(R.id.loading_movies_progress);
        mEmptyView             = (ViewGroup) view.findViewById(R.id.no_movies_view);

        final int movieColumns = getResources().getInteger(R.integer.movies_columns);
        mMoviesRecyclerView.setHasFixedSize(true);
        mManager = new GridLayoutManager(mMoviesRecyclerView.getContext(), movieColumns);
        mManager.setAutoMeasureEnabled(true);
        mMoviesRecyclerView.setLayoutManager(mManager);
        mMoviesRecyclerView.setEmptyView(mEmptyView);

        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SELECTED, mSelected);
        outState.putParcelableArrayList(MOVIES, mMovies);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mMovieAdapter != null && savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(MOVIES);
            mSelected = savedInstanceState.getInt(SELECTED, -1);
        }

        if(mMovies == null) {
            return;
        }

        mMovieAdapter.swap(mMovies);
        mMovieAdapter.setSelected(mSelected);
        mMoviesRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if(mManager != null) {
                    mManager.scrollToPosition(mSelected);
                }
            }
        });
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

        if(MovieUtils.mustReload() || mMovies == null) {
            MoviesNetworkTask networkTask = new MoviesNetworkTask(getActivity(), this);
            networkTask.execute();
        }

        if(mMovieAdapter != null) {
            mMovieAdapter.removeSelected();
        }
    }

    public IMovieClickListener getListener() {
        return mListener;
    }

    public void setListener(IMovieClickListener listener) {
        mListener = listener;
        mMovieAdapter.setListener(mListener);
    }

    @Override
    public void onStartLoad() {
        mLoadingMoviesProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataLoaded(List<Movie> movies) {
        mLoadingMoviesProgress.setVisibility(View.GONE);

        MovieUtils.setReload(false);

        if(mMovieAdapter != null) {
            mMovieAdapter.swap(movies);
            if(mListener != null) {
                Movie movie = mMovieAdapter.getMovies().size() > 0 ? mMovieAdapter.getMovies().get(0) : null;

                if(movie!= null) {
                    mSelected = 0;
                    mMovieAdapter.setSelected(mSelected);
                    mMovieAdapter.notifyDataSetChanged();
                }

                mListener.onMovieClick(movie, true);
            }
        }

        mMovies = new ArrayList<>(movies);
    }

    public interface IMovieClickListener {
        void onMovieClick (Movie movie, boolean forcedByReload);
    }
}
