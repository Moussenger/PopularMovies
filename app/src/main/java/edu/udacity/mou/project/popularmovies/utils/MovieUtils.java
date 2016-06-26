package edu.udacity.mou.project.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import edu.udacity.mou.project.popularmovies.R;

/**
 * Created by Mou on 27/9/15.
 */
public class MovieUtils {
    private static final String LOG_TAG = MovieUtils.class.getSimpleName();
    private static final String SORT_BY = "sort_by";
    private static final String API_KEY = "api_key";

    private static boolean sReload = true;

    public static String getYearOfDate (String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        try {
            return formatter.format(formatter.parse(dateString));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing date", e);
            return "";
        }
    }

    public static Uri getMoviesUri (Context context, String sortParam) {

        return Uri.parse(context.getString(R.string.movies_base_url)).buildUpon()
                .appendQueryParameter(SORT_BY, sortParam)
                .appendQueryParameter(API_KEY, context.getString(R.string.api_key))
                .build();
    }

    public static Uri getImageUri (Context context, String posterCode) {
        return Uri.parse(context.getString(R.string.image_uri)).buildUpon()
                .appendEncodedPath(posterCode)
                .build();
    }

    public static Uri getTrailersUri (Context context, long movieId) {
        String baseUrl = context.getString(R.string.trailers_base_url);
        String url = String.format(baseUrl, movieId);

        return Uri.parse(url).buildUpon()
                .appendQueryParameter(API_KEY, context.getString(R.string.api_key))
                .build();
    }

    public static Uri getYoutubeUri (Context context, String key) {
        return getUriWithKey(context, R.string.youtube_video_uri, key);
    }

    public static Uri getTrailerThumbUri (Context context, String key) {
        return getUriWithKey(context, R.string.youtube_thumb_uri, key);
    }

    private static Uri getUriWithKey(Context context, int urlResource, String key) {
        String baseUrl = context.getString(urlResource);
        String url = String.format(baseUrl, key);

        return Uri.parse(url);
    }

    public static Uri getReviewsUri (Context context, long movieId) {
        String baseUrl = context.getString(R.string.reviews_base_url);
        String url = String.format(baseUrl, movieId);

        return Uri.parse(url).buildUpon()
                .appendQueryParameter(API_KEY, context.getString(R.string.api_key))
                .build();
    }

    public static String getSortParam (Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(
                context.getString(R.string.pref_sort_key),
                context.getString(R.string.most_popular_sort));
    }

    public static boolean mustReload () {
        return sReload;
    }

    public static void setReload (boolean reload) {
        sReload = reload;
    }
}
