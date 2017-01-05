package com.vixir.popularmovies.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.vixir.popularmovies.R;
import com.vixir.popularmovies.adapters.ListMoviesGridAdapter;
import com.vixir.popularmovies.data.MovieColumns;
import com.vixir.popularmovies.data.MovieProvider;
import com.vixir.popularmovies.data.MovieDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FavouriteMovieFragment extends Fragment {

    private ListMoviesGridAdapter mListMoviesGridAdapter;
    private ArrayList mMovieListData = new ArrayList();
    private GridView gridView;
    private static final String[] FAV_MOVIE_COLUMNS = {
            MovieDatabase.MOVIES + "." + MovieColumns._ID,
            MovieColumns.MOVIE_ID,
            MovieColumns.TITLE,
            MovieColumns.SYNOPSIS,
            MovieColumns.RELEASE_DATE,
            MovieColumns.RATING,
            MovieColumns.LANGUAGE,
            MovieColumns.POPULARITY,
            MovieColumns.POSTER_IMAGE,
            MovieColumns.BACKDROP_IMAGE
    };

    static final int COL_MOVIE_ROW_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_SYNOPSIS = 3;
    static final int COL_RELEASE_DATE = 4;
    static final int COL_AVERAGE_RATING = 5;
    static final int COL_LANGUAGE = 6;
    static final int COL_POPULARITY = 7;
    static final int COL_POSTER_IMAGE = 8;
    static final int COL_BACKDROP_IMAGE = 9;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.list_movies_fragment, container, false);
        gridView = (GridView) v.findViewById(R.id.movies_grid);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchMovieListData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        if(mMovieListData!=null){
                    mListMoviesGridAdapter = new ListMoviesGridAdapter(getContext(), mMovieListData);
                    gridView.setAdapter(mListMoviesGridAdapter);
                    mListMoviesGridAdapter.notifyDataSetChanged();
                }
        super.onActivityCreated(savedInstanceState);
    }

    private void fetchMovieListData() {
        Cursor c = getActivity().getContentResolver().query(MovieProvider.buildUri("movies"), null, null, null, null);
        int rowCount = c.getCount();
        c.moveToFirst();
        for(int j=0;j<rowCount;j++)
        {
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("title",c.getString(COL_MOVIE_TITLE));
            map.put("poster",c.getString(COL_POSTER_IMAGE));
            map.put("synopsis",c.getString(COL_SYNOPSIS));
            map.put("voteCount","50");
            map.put("popularity",c.getString(COL_POPULARITY));
            map.put("releaseDate",c.getString(COL_RELEASE_DATE));
            map.put("movieId",c.getString(COL_MOVIE_ID));
            map.put("rating",c.getString(COL_AVERAGE_RATING));
            map.put("language",c.getString(COL_LANGUAGE));
            map.put("backdrop",c.getString(COL_BACKDROP_IMAGE));
            c.moveToNext();
        mMovieListData.add(map);
        }
    }
}
