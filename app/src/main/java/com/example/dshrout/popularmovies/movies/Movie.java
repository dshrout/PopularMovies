package com.example.dshrout.popularmovies.movies;

public class Movie {
    private String mId;
    private String mPosterPath;

    public Movie(){}

    public String getId() {return mId;}
    public void setId(String id) {mId = id;}
    public String getPosterPath() {return mPosterPath;}
    public void setPosterPath(String path) {mPosterPath = path;}
}
