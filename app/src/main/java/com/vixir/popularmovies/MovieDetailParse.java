package com.vixir.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vidhya on 10-10-2016.
 */
public class MovieDetailParse implements Parcelable {
    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverView() {
        return overView;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getMovieId() {
        return movieId;
    }
    public String getRating() {
        return rating;
    }
    public String getLanguage() {
        return language;
    }
    public String getBackdrop() {
        return backdrop;
    }


    private String title;
    private String poster;
    private String overView;
    private String voteCount;
    private String popularity;
    private String releaseDate;
    private String movieId;
    private String rating;
    private String language;
    private String backdrop;


    public MovieDetailParse( String title, String movieId, String poster, String overView, String voteCount, String popularity, String releaseDate, String rating, String language, String backdrop) {
        this.title = title;
        this.movieId = movieId;
        this.poster = poster;
        this.overView = overView;
        this.voteCount = voteCount;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.language = language;
        this.backdrop = backdrop;
    }

    protected MovieDetailParse(Parcel in) {
        title = in.readString();
        movieId = in.readString();
        poster = in.readString();
        overView = in.readString();
        voteCount = in.readString();
        popularity = in.readString();
        releaseDate = in.readString();
        rating = in.readString();
        language = in.readString();
        backdrop = in.readString();
    }

    public static final Creator<MovieDetailParse> CREATOR = new Creator<MovieDetailParse>() {
        @Override
        public MovieDetailParse createFromParcel(Parcel in) {
            return new MovieDetailParse(in);
        }

        @Override
        public MovieDetailParse[] newArray(int size) {
            return new MovieDetailParse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(movieId);
        dest.writeString(poster);
        dest.writeString(overView);
        dest.writeString(voteCount);
        dest.writeString(popularity);
        dest.writeString(releaseDate);
        dest.writeString(rating);
        dest.writeString(language);
        dest.writeString(backdrop);
    }
}
