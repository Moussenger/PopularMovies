package edu.udacity.mou.project.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

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

        mMovieAdapter.addAll(getMockData());

        mMoviesGridView.setAdapter(mMovieAdapter);

        return view;
    }

    private ArrayList<String> getMockData () {
        ArrayList<String> mockData = new ArrayList<>();

        for(int i=0; i<10; i++) {
            mockData.add("nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
        }

        return mockData;
    }
}
