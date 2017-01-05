package com.vixir.popularmovies;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;

import com.vixir.popularmovies.fragments.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.MOVIE_DETAIL_URI, getIntent().getData());
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            Transition changeTransform = TransitionInflater.from(this).
                    inflateTransition(R.transition.change_image_transform);
            Transition explodeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.explode);
            fragment.setExitTransition(explodeTransform);
            fragment.setSharedElementReturnTransition(changeTransform);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.detail_placeholder, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.supportFinishAfterTransition();
    }
}
