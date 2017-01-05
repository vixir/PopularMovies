package com.vixir.popularmovies.data;


import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface MovieColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TITLE = "title";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String SYNOPSIS = "synopsis";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String RATING = "rating";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String LANGUAGE = "language";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String POPULARITY = "popularity";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String POSTER_IMAGE = "poster_image";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String BACKDROP_IMAGE = "backdrop_image";


}
