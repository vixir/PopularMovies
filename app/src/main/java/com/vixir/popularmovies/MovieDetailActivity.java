package com.vixir.popularmovies;

import android.annotation.TargetApi;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
//android.support.v7.graphics.Palette;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vixir.popularmovies.adapters.CommentAdapter;
import com.vixir.popularmovies.data.MovieColumns;
import com.vixir.popularmovies.data.MovieProvider;
import com.vixir.popularmovies.retro.Example;
import com.vixir.popularmovies.retro.Result;
import com.vixir.popularmovies.retro.TrailerAPI;
import com.vixir.popularmovies.type.Trailer;
import com.vixir.popularmovies.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v7.appcompat.R.styleable.TextAppearance;

/**
 * Created by Vivek on 10-10-2016.
 */
public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener, Callback<Example> {
    private static final String FORECAST_SHARE_HASHTAG = "#Popular Movies";
    private ImageView poster_imageview;
    private TextView synopsis_textview;
    private TextView rating_textview;
    private TextView language_textview;
    private TextView popularity_textview;
    private ImageView backdropImageView;
    private String LOG_TAG = MovieDetailActivity.class.getName();
    private RecyclerView trailer_recycler;
    private RecyclerView comments_recycler;
    private TrailerAdapter trailerAdapter;
    private CommentAdapter mCommentAdapter;
    private LinearLayout favouriteLinearLayout;
    private LinearLayout shareLinearLayout;
    private MovieDetailParse mMovieDetailParseObject;
    private String mShareMovieDetails;
    private TextView toolbarTitle;
    private CollapsingToolbarLayout collapToolbar;
    private boolean isAlreadyFav = false;
    private TextView fav_textview;
    private ImageView favIcon;
    private Palette palette;
    private static Palette.Swatch swatch;
    private Target target = new Target() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            backdropImageView.setImageBitmap(bitmap);
            palette = Palette.generate(bitmap);
            swatch = palette.getVibrantSwatch();
            if (swatch != null) {
                detailParent.setBackgroundColor(swatch.getRgb());
                getWindow().setStatusBarColor(swatch.getRgb());
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    private CoordinatorLayout detailParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.movie_detail_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbr));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        detailParent = (CoordinatorLayout) findViewById(R.id.coordinator_parent);
        poster_imageview = (ImageView) findViewById(R.id.poster_detail);
        synopsis_textview = (TextView) findViewById(R.id.synopsis_detail);
        rating_textview = (TextView) findViewById(R.id.rating_detail);
        fav_textview = (TextView) findViewById(R.id.fav_added);
        language_textview = (TextView) findViewById(R.id.language);
        popularity_textview = (TextView) findViewById(R.id.popularity);
        favIcon = (ImageView) findViewById(R.id.fav_icon);
        trailer_recycler = (RecyclerView) findViewById(R.id.trailer_recycler);
        comments_recycler = (RecyclerView) findViewById(R.id.comments_recycler);
        backdropImageView = (ImageView) findViewById(R.id.backdrop);
        shareLinearLayout = (LinearLayout) findViewById(R.id.share);
        collapToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mMovieDetailParseObject = getIntent().getParcelableExtra("movieDetailParse");

        Picasso.with(this).load(Util.getImageUrl(mMovieDetailParseObject.getPoster())).into(poster_imageview);
        Picasso.with(this).load(Util.getImageUrl(mMovieDetailParseObject.getBackdrop())).into(target);

        toolbarTitle.setText(mMovieDetailParseObject.getTitle());
        synopsis_textview.setText(mMovieDetailParseObject.getOverView());
        rating_textview.setText(mMovieDetailParseObject.getRating());
        language_textview.setText(mMovieDetailParseObject.getLanguage());
        popularity_textview.setText(getPopularity(mMovieDetailParseObject.getPopularity()));
        Cursor c = MovieDetailActivity.this.getContentResolver().query(MovieProvider.Movies.CONTENT_URI, null, MovieColumns.MOVIE_ID + "= ?", new String[]{mMovieDetailParseObject.getMovieId()}, null);
        if (c != null && c.getCount() > 0) {
            isAlreadyFav = true;
            fav_textview.setText("Favourites");
            favIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        }
        collapToolbar.setTitle("");
        favouriteLinearLayout = (LinearLayout) findViewById(R.id.favourite);
        shareLinearLayout.setOnClickListener(this);
        favouriteLinearLayout.setOnClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager mLayoutManagerComm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        comments_recycler.setLayoutManager(mLayoutManagerComm);
        trailer_recycler.setLayoutManager(mLayoutManager);
        mCommentAdapter = new CommentAdapter(this, mMovieDetailParseObject.getMovieId());
        comments_recycler.setAdapter(mCommentAdapter);
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TrailerAPI.ENDPOINT).addConverterFactory(GsonConverterFactory.create(gson)).build();
        TrailerAPI githubUserAPI = retrofit.create(TrailerAPI.class);
        Call<Example> callUser = githubUserAPI.getUser(mMovieDetailParseObject.getMovieId());
        mShareMovieDetails = String.format(" Hey check it out!! \n %s movie - has %s rating and has %s votes", mMovieDetailParseObject.getTitle(), mMovieDetailParseObject.getRating(), mMovieDetailParseObject.getVoteCount());
        callUser.enqueue(this);
        super.onCreate(savedInstanceState);

    }

    private String getPopularity(String popularity) {
        if (popularity.length() > 4) {
            popularity = popularity.substring(0, 5);
        }
        return popularity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
          onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favourite: {
                if (isAlreadyFav) {
                    int d = MovieDetailActivity.this.getContentResolver().delete(MovieProvider.Movies.CONTENT_URI, MovieColumns.MOVIE_ID + "= ?", new String[]{mMovieDetailParseObject.getMovieId()});
                    favIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    isAlreadyFav = false;
                } else {
                    favIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    insertData();
                    isAlreadyFav = true;
                }
                return;
            }
            case R.id.share: {
                Intent intent = createShareForecastIntent();
                startActivity(intent);
                return;
            }
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareMovieDetails + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    private void insertData() {
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(MovieProvider.Movies.CONTENT_URI);
        builder.withValue(MovieColumns.TITLE, mMovieDetailParseObject.getTitle());
        builder.withValue(MovieColumns.MOVIE_ID, mMovieDetailParseObject.getMovieId());
        builder.withValue(MovieColumns.SYNOPSIS, mMovieDetailParseObject.getOverView());
        builder.withValue(MovieColumns.POPULARITY, mMovieDetailParseObject.getPopularity());
        builder.withValue(MovieColumns.RELEASE_DATE, mMovieDetailParseObject.getReleaseDate());
        builder.withValue(MovieColumns.RATING, mMovieDetailParseObject.getRating());
        builder.withValue(MovieColumns.LANGUAGE, mMovieDetailParseObject.getLanguage());
        builder.withValue(MovieColumns.POSTER_IMAGE, mMovieDetailParseObject.getPoster());
        builder.withValue(MovieColumns.BACKDROP_IMAGE, mMovieDetailParseObject.getBackdrop());
        batchOperations.add(builder.build());
        try {
            MovieDetailActivity.this.getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }

    @Override
    public void onResponse(Call<Example> call, Response<Example> response) {
        if (null == response) {
            return;
        }
        Example example = response.body();
        List list = example.getResults();
        trailerAdapter = new TrailerAdapter(this, list);
        trailer_recycler.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<Example> call, Throwable t) {

    }
}
