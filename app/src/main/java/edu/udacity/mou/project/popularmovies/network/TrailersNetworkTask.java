package edu.udacity.mou.project.popularmovies.network;

import android.content.Context;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.udacity.mou.project.popularmovies.R;
import edu.udacity.mou.project.popularmovies.helpers.DataHelper;
import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.model.Trailer;
import edu.udacity.mou.project.popularmovies.utils.MovieUtils;

/**
 * Created by Mou on 27/9/15.
 */
public class TrailersNetworkTask extends AbstractGeneralMoviesNetworkTask<Trailer> {
    private final String KEY  = "key";
    private final String NAME = "name";

    private Movie mMovie;

    public TrailersNetworkTask(Context context, Movie movie, INetworkListener listener) {
        super(context, listener);
        mMovie = movie;
    }

    @Override
    protected List<Trailer> getDataFromCache() {
        return null;
    }

    @Override
    protected Uri getUri() {
        return MovieUtils.getTrailersUri(getContext(), mMovie.getId());
    }

    @Override
    protected Trailer parseObject(JSONObject object) throws JSONException {
        return new Trailer.Builder()
                .setKey(object.getString(KEY))
                .setName(object.getString(NAME))
                .build();
    }
}
