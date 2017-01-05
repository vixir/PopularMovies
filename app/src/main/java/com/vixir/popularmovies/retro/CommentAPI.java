package com.vixir.popularmovies.retro;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface CommentAPI {
    String ENDPOINT = "http://api.themoviedb.org";
    //remove key from here
    @GET("/3/movie/{movieId}/reviews")
    Call<CommentResponse> getUser(@Path("movieId") String movieId,@Query("api_key")String apiKey );
}