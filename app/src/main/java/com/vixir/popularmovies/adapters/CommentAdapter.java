package com.vixir.popularmovies.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vixir.popularmovies.R;
import com.vixir.popularmovies.TrailerAdapter;
import com.vixir.popularmovies.retro.CommentAPI;
import com.vixir.popularmovies.retro.Example;
import com.vixir.popularmovies.retro.TrailerAPI;
import com.vixir.popularmovies.type.CommentResponse;
import com.vixir.popularmovies.type.CommentResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL on 19-12-2016.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> implements Callback<CommentResponse> {

    private Context mContext;
    private String mMovieId;
    private List commentDetailsList;
    private static final int MAX_LINES = 5;
    boolean showMore = true;
    TextView noComments;

    public CommentAdapter(Context context, String movieId, TextView noComments) {
        this.mContext = context;
        this.mMovieId = movieId;
        this.noComments = noComments;
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(TrailerAPI.ENDPOINT).addConverterFactory(GsonConverterFactory.create(gson)).build();
        CommentAPI githubUserAPI = retrofit.create(CommentAPI.class);
        Call<CommentResponse> callUser = githubUserAPI.getUser(movieId);
        callUser.enqueue(this);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_vertical, parent, false);
        return new CommentAdapter.CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        CommentResult commentRs = (CommentResult) commentDetailsList.get(position);
        holder.commentPerson.setText(commentRs.getAuthor());
        holder.commentContent.setText(commentRs.getContent());
        holder.commentContent.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = holder.commentContent.getLineCount();
                if (lineCount > CommentAdapter.MAX_LINES) {
                    holder.showMore.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showMore == true) {
                    ObjectAnimator animation = ObjectAnimator.ofInt(holder.commentContent, "maxLines", holder.commentContent.getLineCount());
                    animation.setDuration(200).start();
                    holder.showMore.setText("show less");
                    showMore = false;
                } else {
                    ObjectAnimator animation = ObjectAnimator.ofInt(holder.commentContent, "maxLines", CommentAdapter.MAX_LINES);
                    animation.setDuration(200).start();
                    holder.showMore.setText("show more");
                    showMore = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (commentDetailsList != null) return commentDetailsList.size();
        return 0;
    }

    @Override
    public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
        if (null != response) {
            CommentResponse commentResponse = response.body();
            commentDetailsList = commentResponse.getResults();
            this.notifyDataSetChanged();
        }
        if (commentDetailsList != null && commentDetailsList.size() == 0) {
            noComments.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(Call<CommentResponse> call, Throwable t) {
        noComments.setVisibility(View.VISIBLE);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentContent;
        public TextView commentPerson;
        public TextView showMore;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentContent = (TextView) itemView.findViewById(R.id.comment_content);
            commentPerson = (TextView) itemView.findViewById(R.id.comment_person);
            showMore = (TextView) itemView.findViewById(R.id.show_more);
        }
    }

}
