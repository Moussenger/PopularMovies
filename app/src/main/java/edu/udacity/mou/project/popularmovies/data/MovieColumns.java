package edu.udacity.mou.project.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by mou on 18/06/16.
 */
public interface MovieColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String ORIGINAL_TITLE = "original_title";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String MOVIE_POSTER = "movie_poster";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String SYNOPSIS = "synopsis";

    @DataType(DataType.Type.REAL)
    @NotNull
    String USER_RATING = "user_rating";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String RELEASE_DATE = "release_date";
}
