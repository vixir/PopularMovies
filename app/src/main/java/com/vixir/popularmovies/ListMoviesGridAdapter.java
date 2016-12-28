package com.vixir.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vixir.popularmovies.utils.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vivek on 09-10-2016.
 */
public class ListMoviesGridAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Map<String, String>> movieListData = new ArrayList<>();
    private Palette palette;
    private static Palette.Swatch swatch;

    private Target getTarget(final ImageView imageView, final  TextView textView) {
        return new Target() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                palette = Palette.generate(bitmap);
                swatch = palette.getDarkVibrantSwatch();
                if (swatch != null) {
                    textView.setSelected(true);
                    textView.setBackgroundColor(swatch.getRgb());
                    textView.setTextColor(swatch.getTitleTextColor());
                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

    ListMoviesGridAdapter(Context context, ArrayList movieListData) {
        this.context = context;
        this.movieListData = movieListData;
    }

    @Override
    public int getCount() {
        return movieListData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View moviesGridView;
        moviesGridView = inflater.inflate(R.layout.grid_item, null);
        if (moviesGridView != null) {
            final ImageView imageView = (ImageView) moviesGridView.findViewById(R.id.grid_item_image);
            final TextView textView = (TextView) moviesGridView.findViewById(R.id.title);
            Map<String, String> map = (Map<String, String>) movieListData.get(position);
            String title = map.get("title");
            textView.setText(title);
            String poster = map.get("poster");
            String overView = map.get("synopsis");
            String voteCount = map.get("voteCount");
            String popularity = map.get("popularity");
            String releaseDate = map.get("releaseDate").split("-")[0];
            String movieId = map.get("movieId");
            String rating = map.get("rating");
            String language = map.get("language");
            String backdrop = map.get("backdrop");

            Picasso.with(context).load(Util.getImageUrl(map.get("poster"))).into(imageView);
            Picasso.with(context).load(Util.getImageUrl(map.get("poster"))).into(getTarget(imageView, textView));
            final MovieDetailParse movieDetailParseObject = new MovieDetailParse(title, movieId, poster, overView, voteCount, popularity, releaseDate, rating, language, backdrop);

            moviesGridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListMovieFragment.OnPosterClicked) context).onPosterSelected(movieDetailParseObject);
                }
            });
        }
        return moviesGridView;
    }
}
