package com.vixir.popularmovies.retro;

import com.vixir.popularmovies.type.CommentResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by DELL on 19-12-2016.
 */

public interface CommentAPI {
    String ENDPOINT = "http://api.themoviedb.org";
    //remove key from here
    @GET("/3/movie/{movieId}/reviews?api_key=78e10d5301f643b76132741aa2d01aee")
    Call<CommentResponse> getUser(@Path("movieId") String movieId);
}
