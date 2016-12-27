package com.vixir.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;
import com.vixir.popularmovies.fragments.MovieDetailFragment;
import com.vixir.popularmovies.sharedpref.SettingsActivity;

public class MainActivity extends AppCompatActivity implements ListMovieFragment.OnPosterClicked {

    private static final String MOVIEDETAIL_TAG = "MDTAG";
    private static final String LISTMOVIE_TAG = "LMTAG";
    private static final String FAVOURITEMOVIE_TAG = "FMTAG";
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String orderByConnPref = sharedPref.getString(SettingsActivity.KEY_SORT_ORDER, "");
        if (findViewById(R.id.detail_placeholder) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_placeholder, new MovieDetailFragment(), MOVIEDETAIL_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        if (orderByConnPref.equals("favourites")) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_placeholder, new FavouriteMovieFragment(),FAVOURITEMOVIE_TAG);
            fragmentTransaction.commit();
        } else {
            Bundle args = new Bundle();
            args.putBoolean("twoPane", mTwoPane);
            args.putParcelable("calling", getCallingActivity());
            ListMovieFragment listMovieFragment = new ListMovieFragment();
            listMovieFragment.setArguments(args);
            Transition changeTransform = TransitionInflater.from(this).
                    inflateTransition(R.transition.change_image_transform);
            Transition explodeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.explode);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            listMovieFragment.setExitTransition(explodeTransform);
            listMovieFragment.setSharedElementReturnTransition(changeTransform);
            fragmentTransaction.replace(R.id.fragment_placeholder, listMovieFragment, LISTMOVIE_TAG);
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPosterSelected(MovieDetailParse movieDetailParseObject) {
        if(mTwoPane == false){
            Intent intent = new Intent(this, MovieDetailActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this , findViewById(R.id.image_container) , "poster");
            intent.putExtra("movieDetailParse",movieDetailParseObject);
            this.startActivity(intent, options.toBundle());
        }else{
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putParcelable("movieDetailParse",movieDetailParseObject);
            movieDetailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.detail_placeholder, movieDetailFragment, MOVIEDETAIL_TAG)
                                        .commit();
        }
    }
}
