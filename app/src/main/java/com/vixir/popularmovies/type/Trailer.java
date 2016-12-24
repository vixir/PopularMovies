package com.vixir.popularmovies.type;

/**
 * Created by DELL on 18-12-2016.
 */

public class Trailer {
    public Trailer(){}
    public Trailer(String name,String key){
        this.name = name;
        this.key = key;
    }
    private String name, key;

    public String getTrailerUrl() {
        return key;
    }

    public void setTrailerUrl(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
