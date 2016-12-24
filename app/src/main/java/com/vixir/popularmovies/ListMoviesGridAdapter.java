package com.vixir.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vixir.popularmovies.utils.Util;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Vivek on 09-10-2016.
 */
public class ListMoviesGridAdapter extends BaseAdapter {

    private Context context;
    ArrayList <Map<String,String>>movieListData;

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
            Map<String, String> map = (Map<String, String>) movieListData.get(position);
            String title = map.get("title");
            String poster = map.get("poster");
            String overView = map.get("synopsis");
            String voteCount = map.get ("voteCount");
            String popularity = map.get("popularity");
            String releaseDate = map.get("releaseDate").split("-")[0];
            String movieId = map.get("movieId");
            String rating = map.get("rating");
            String language = map.get("language");
            String backdrop = map.get("backdrop");
            Picasso.with(context).load(Util.getImageUrl(map.get("poster"))).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
            final MovieDetailParse movieDetailParseObject = new MovieDetailParse(title,movieId,poster,overView,voteCount,popularity,releaseDate,rating,language,backdrop);
            moviesGridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, moviesGridView, "poster");
                    intent.putExtra("movieDetailParse",movieDetailParseObject);
                    context.startActivity(intent,options.toBundle());
                }
            });
        }
        return moviesGridView;
    }
}
