package edu.udacity.mou.project.popularmovies.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import edu.udacity.mou.project.popularmovies.R;
import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;

/**
 * Created by Mou on 26/9/15.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {

    public MoviesAdapter(Context context) {
        super(context, R.layout.item_movie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MovieHolder movieHolder;

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
            movieHolder = new MovieHolder(view);
        } else {
            movieHolder = (MovieHolder) view.getTag();
        }

        setMovieImage(position, movieHolder);
        view.setTag(movieHolder);

        return view;
    }

    private void setMovieImage (int position, MovieHolder holder) {
        Picasso.with(getContext())
            .load(MovieUtils.getImageUri(getContext(), getItem(position).getMoviePoster()))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_placeholder)
            .fit()
            .centerInside()
            .into(holder.movieImage);

    }

    private static class MovieHolder {
        private ImageView movieImage;

        public MovieHolder(View view) {
            movieImage = (ImageView) view.findViewById(R.id.movie_image);
        }
    }
}
