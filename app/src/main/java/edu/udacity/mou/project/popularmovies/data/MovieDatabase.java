package edu.udacity.mou.project.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by mou on 18/06/16.
 */
@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {
    public static final int VERSION = 1;

    @Table(MovieColumns.class)
    public static final String FAVORITES = "favorites";

    private MovieDatabase() {}
}
