package edu.udacity.mou.project.popularmovies.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.mou.project.popularmovies.MoviesFragment;
import edu.udacity.mou.project.popularmovies.R;
import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.model.Review;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;

/**
 * Created by Mou on 26/9/15.
 */
public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieHolder> {
    private int mSelected = -1;
    private MoviesFragment.IMovieClickListener mListener;
    private ArrayList<Movie> mMovies;
    private Context mContext;
    private boolean mHasSelect;

    public MoviesRecyclerViewAdapter(Context context, boolean hasSelect) {
        mContext = context;
        mMovies = new ArrayList<>();
        mHasSelect = hasSelect;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieHolder movieHolder, final int position) {
        final Movie movie = mMovies.get(position);

        setMovieImage(movie, movieHolder);
        setMovieSelected(movieHolder, position);

        movieHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelected = position;

                if(mListener != null) {
                    mListener.onMovieClick(movie, false);
                }

                if(mHasSelect) {
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public int getSelected() {
        return mSelected;
    }

    public void setSelected (int position) {
        mSelected = position;
    }

    public void removeSelected () {
        setSelected(-1);
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }

    public void swap(List<Movie> newMovies){
        mMovies.clear();

        if(mMovies != null && newMovies != null) {
            mMovies.addAll(newMovies);
        }

        notifyDataSetChanged();
    }

    public void setListener (MoviesFragment.IMovieClickListener listener) {
        mListener = listener;
    }

    private void setMovieImage (Movie movie, MovieHolder holder) {
        Picasso.with(mContext)
            .load(MovieUtils.getImageUri(mContext, movie.getMoviePoster()))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_placeholder)
            .resize(calcSizeOnDp(600), calcSizeOnDp(400))
            .centerInside()
            .into(holder.mMovieImage);

    }

    private void setMovieSelected (MovieHolder movieHolder, int position) {
        if(mSelected == position && mHasSelect) {
            movieHolder.mSelectedImage.setVisibility(View.VISIBLE);
        } else {
            movieHolder.mSelectedImage.setVisibility(View.GONE);
        }
    }

    private int calcSizeOnDp (int px) {
        Resources r = mContext.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, r.getDisplayMetrics());
    }

    private void setSelectedImage (int position, MovieHolder movieHolder) {
        if(position == mSelected) {
            movieHolder.mSelectedImage.setVisibility(View.VISIBLE);
        } else {
            movieHolder.mSelectedImage.setVisibility(View.GONE);
        }
    }

    public static class MovieHolder extends RecyclerView.ViewHolder {
        private ImageView mMovieImage;
        private ImageView mSelectedImage;
        private View mView;

        public MovieHolder(View view) {
            super(view);
            mView = view;
            mMovieImage = (ImageView) view.findViewById(R.id.movie_image);
            mSelectedImage = (ImageView) view.findViewById(R.id.movie_selected_image);
        }
    }
}
