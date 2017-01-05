package com.vixir.popularmovies.retro;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrailerAPI {
    String ENDPOINT = "http://api.themoviedb.org";

    //remove key from here
    @GET("/3/movie/{movieId}/videos")
    Call<TrailerResponse> getUser( @Path("movieId") String movieId,@Query("api_key")String apiKey);
}
