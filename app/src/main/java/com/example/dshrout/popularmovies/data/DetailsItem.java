package com.example.dshrout.popularmovies.data;

/**
 * Created by DShrout on 10/13/2015.
 */
public class DetailsItem {
    public int id;
    public String title;
    public String tagline;
    public String poster_path;
    public String backdrop_path;
    public String overview;
    public String release_date;
    public String runtime;
    public String popularity;
    public String vote_average;
    public String vote_count;

    public DetailsItem() {
        id = -1;
        title = "";
        tagline = "";
        poster_path = "";
        backdrop_path = "";
        overview = "";
        release_date = "";
        runtime = "";
        popularity = "";
        vote_average = "";
        vote_count = "";
    }
}
