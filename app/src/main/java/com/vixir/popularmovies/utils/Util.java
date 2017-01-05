package com.vixir.popularmovies.utils;

import com.vixir.popularmovies.BuildConfig;


public class Util {
    private static String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private static String IMAGE_URL_BACK_DROP = "http://image.tmdb.org/t/p/original/";
    public static final String MY_TMDB_API_KEY = BuildConfig.TMDB_API_KEY;
    public static String getImageUrl(String imagePath) {
        return IMAGE_URL+imagePath;
    }
    public static String getImageUrlForBackDrop(String imagePath) {
        return IMAGE_URL_BACK_DROP+imagePath;
    }
}
