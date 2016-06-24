package edu.udacity.mou.project.popularmovies.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import edu.udacity.mou.project.popularmovies.R;
import edu.udacity.mou.project.popularmovies.model.Movie;
import edu.udacity.mou.project.popularmovies.model.Trailer;

/**
 * Created by mou on 23/06/16.
 */
public class IntentUtils {
    private static final String TEXT_PLAIN = "text/plain";

    public static void watchTrailer (Context context, String key) {
        context.startActivity(getIntentForNewActivity(MovieUtils.getYoutubeUri(context, key).toString()));
    }

    public static Intent getTrailerShareIntent (Context context, Movie movie, Trailer trailer) {
        String name = trailer.getName();
        String title = movie.getOriginalTitle();
        String url = MovieUtils.getYoutubeUri(context, trailer.getKey()).toString();

        String baseShareText = context.getString(R.string.share_trailer_text);
        String shareText = String.format(baseShareText, name, title, url);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        intent.setType(TEXT_PLAIN);
        intent.putExtra(Intent.EXTRA_TEXT, shareText);

        return intent;
    }

    private static Intent getIntentForNewActivity(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }
}
