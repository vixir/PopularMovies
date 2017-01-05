
package com.vixir.popularmovies.retro;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailerResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailerResult> trailerResults = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailerResult> getTrailerResults() {
        return trailerResults;
    }

    public void setTrailerResults(List<TrailerResult> trailerResults) {
        this.trailerResults = trailerResults;
    }

}
