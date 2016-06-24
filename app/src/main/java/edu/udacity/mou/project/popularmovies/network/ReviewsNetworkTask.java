package edu.udacity.mou.project.popularmovies.network;

import android.content.Context;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.model.Review;
import edu.udacity.mou.project.popularmovies.model.Trailer;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;

/**
 * Created by Mou on 27/9/15.
 */
public class ReviewsNetworkTask extends AbstractGeneralMoviesNetworkTask<Review> {
    private final String AUTHOR  = "author";
    private final String CONTENT = "content";

    private Movie mMovie;

    public ReviewsNetworkTask(Context context, Movie movie, INetworkListener listener) {
        super(context, listener);
        mMovie = movie;
    }

    @Override
    protected List<Review> getDataFromCache() {
        return null;
    }

    @Override
    protected Uri getUri() {
        return MovieUtils.getReviewsUri(getContext(), mMovie.getId());
    }

    @Override
    protected Review parseObject(JSONObject object) throws JSONException {
        return new Review.Builder()
                .setAuthor(object.getString(AUTHOR))
                .setContent(object.getString(CONTENT))
                .build();
    }
}
