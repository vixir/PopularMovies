package com.vixir.popularmovies.data.gen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.vixir.popularmovies.data.MovieColumns;
import java.lang.Override;
import java.lang.String;

public class MovieDatabase extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "movieDatabase.db";
  public static final String TABLE_MOVIES = "movies";
  public static final String MOVIES = "CREATE TABLE movies ("
   + MovieColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
   + MovieColumns.MOVIE_ID + " TEXT NOT NULL,"
   + MovieColumns.TITLE + " TEXT NOT NULL,"
   + MovieColumns.SYNOPSIS + " TEXT NOT NULL,"
   + MovieColumns.RELEASE_DATE + " TEXT NOT NULL,"
   + MovieColumns.RATING + " TEXT NOT NULL,"
   + MovieColumns.LANGUAGE + " TEXT NOT NULL,"
   + MovieColumns.POPULARITY + " TEXT NOT NULL,"
   + MovieColumns.POSTER_IMAGE + " TEXT NOT NULL,"
   + MovieColumns.BACKDROP_IMAGE + " TEXT NOT NULL)";

  private static volatile MovieDatabase instance;

  private Context context;

  private MovieDatabase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }

  public static MovieDatabase getInstance(Context context) {
    if (instance == null) {
      synchronized (MovieDatabase.class) {
        if (instance == null) {
          instance = new MovieDatabase(context);
        }
      }
    }
    return instance;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(MOVIES);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS "+ MOVIES);
    onCreate(db);
  }
}
