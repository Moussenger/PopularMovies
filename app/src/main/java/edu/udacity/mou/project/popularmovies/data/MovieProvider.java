package edu.udacity.mou.project.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by mou on 18/06/16.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {
    public static final String AUTHORITY = "edu.udacity.mou.project.popularmovies.data.MovieProvider";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String FAVORITES = "favorites";
    }

    private static Uri buildUri (String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();

        for (String path : paths) {
            builder.appendPath(path);
        }

        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.FAVORITES)
    public static class Favorites {
        @ContentUri(
                path = Path.FAVORITES,
                type = "vnd.android.cursor.dir/movie",
                defaultSort = MovieColumns.USER_RATING + " DESC")
        public static final Uri CONTENT_URI = buildUri(Path.FAVORITES);

        @InexactContentUri(
                name = "FAVORITE_ID",
                path = Path.FAVORITES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId (long id) {
            return buildUri(Path.FAVORITES, String.valueOf(id));
        }
    }
}
