package com.vixir.popularmovies.data.gen.values;

import android.content.ContentValues;
import com.vixir.popularmovies.data.MovieColumns;
import java.lang.String;

public class MoviesValuesBuilder {
  ContentValues values = new ContentValues();

  public MoviesValuesBuilder Id(int value) {
    values.put(MovieColumns._ID, value);
    return this;
  }

  public MoviesValuesBuilder Id(long value) {
    values.put(MovieColumns._ID, value);
    return this;
  }

  public MoviesValuesBuilder movieId(String value) {
    values.put(MovieColumns.MOVIE_ID, value);
    return this;
  }

  public MoviesValuesBuilder title(String value) {
    values.put(MovieColumns.TITLE, value);
    return this;
  }

  public MoviesValuesBuilder synopsis(String value) {
    values.put(MovieColumns.SYNOPSIS, value);
    return this;
  }

  public MoviesValuesBuilder releaseDate(String value) {
    values.put(MovieColumns.RELEASE_DATE, value);
    return this;
  }

  public MoviesValuesBuilder rating(String value) {
    values.put(MovieColumns.RATING, value);
    return this;
  }

  public MoviesValuesBuilder language(String value) {
    values.put(MovieColumns.LANGUAGE, value);
    return this;
  }

  public MoviesValuesBuilder popularity(String value) {
    values.put(MovieColumns.POPULARITY, value);
    return this;
  }

  public MoviesValuesBuilder posterImage(String value) {
    values.put(MovieColumns.POSTER_IMAGE, value);
    return this;
  }

  public MoviesValuesBuilder backdropImage(String value) {
    values.put(MovieColumns.BACKDROP_IMAGE, value);
    return this;
  }

  public ContentValues values() {
    return values;
  }
}
