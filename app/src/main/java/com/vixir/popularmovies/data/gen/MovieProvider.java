package com.vixir.popularmovies.data.gen;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.vixir.popularmovies.data.MovieColumns;

import java.lang.IllegalArgumentException;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;

public class MovieProvider extends ContentProvider {
    public static final String AUTHORITY = "com.vixir.popularmovies.MovieProvider";

    private static final int MOVIES_CONTENT_URI = 0;

    private static final int MOVIES_PLANET_ID = 1;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final Uri CONTENT_URI = buildUri(MovieDatabase.TABLE_MOVIES);

    static {
        MATCHER.addURI(AUTHORITY, "movies", MOVIES_CONTENT_URI);
        MATCHER.addURI(AUTHORITY, "movies/#", MOVIES_PLANET_ID);
    }

    private SQLiteOpenHelper database;

    public static Uri buildUri(String ...paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for(String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @Override
    public boolean onCreate() {
        database = MovieDatabase.getInstance(getContext());
        return true;
    }


    private long[] insertValues(SQLiteDatabase db, String table, ContentValues[] values) {
        long[] ids = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            ContentValues cv = values[i];
            db.insertOrThrow(table, null, cv);
        }
        return ids;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            switch (MATCHER.match(uri)) {
                case MOVIES_CONTENT_URI: {
                    long[] ids = insertValues(db, "movies", values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
                case MOVIES_PLANET_ID: {
                    long[] ids = insertValues(db, "movies", values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return values.length;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> ops) throws OperationApplicationException {
        ContentProviderResult[] results;
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            results = super.applyBatch(ops);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return results;
    }

    @Override
    public String getType(Uri uri) {
           return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(MovieDatabase.TABLE_MOVIES);
        final SQLiteDatabase db = database.getReadableDatabase();
        switch (MATCHER.match(uri)) {
            case MOVIES_CONTENT_URI: {
                if (sortOrder == null) {
                    sortOrder = "release_date ASC";
                }
                final String groupBy = null;
                final String having = null;
                final String limit = null;
//                sqLiteQueryBuilder.appendWhere(MovieColumns._ID + "=" + uri.getLastPathSegment());
                Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            case MOVIES_PLANET_ID: {
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case MOVIES_CONTENT_URI: {
                final long id = db.insertOrThrow("movies", null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
            case MOVIES_PLANET_ID: {
                final long id = db.insertOrThrow("movies", null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (MATCHER.match(uri)) {
            case MOVIES_CONTENT_URI: {
                rowsUpdated = db.update(MovieDatabase.TABLE_MOVIES, values, where, whereArgs);
                if (rowsUpdated > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;
            }
            case MOVIES_PLANET_ID: {
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    rowsUpdated = db.update(MovieDatabase.TABLE_MOVIES, values, MovieColumns._ID + "=" + id, null);
                } else {
                    rowsUpdated = db.update(MovieDatabase.TABLE_MOVIES, values, MovieColumns._ID + "=" + id + " and " + where, whereArgs);
                }
                if (rowsUpdated > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (MATCHER.match(uri)) {
            case MOVIES_CONTENT_URI: {
                rowsDeleted = db.delete(MovieDatabase.TABLE_MOVIES, where, whereArgs);
                if (rowsDeleted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            }
            case MOVIES_PLANET_ID: {
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    rowsDeleted = db.delete(MovieDatabase.TABLE_MOVIES, MovieColumns._ID + "=" + id, null);
                } else {
                    rowsDeleted = db.delete(MovieDatabase.TABLE_MOVIES, MovieColumns._ID + "=" + id + " and " + where, whereArgs);
                }
                if (rowsDeleted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }
}
