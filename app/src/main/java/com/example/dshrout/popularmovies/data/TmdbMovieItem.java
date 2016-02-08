package com.example.dshrout.popularmovies.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DShrout on 10/13/2015.
 */
public class TmdbMovieItem {
    public boolean adult;
    public String backdrop_path;
    public List<Integer> genre_ids;
    public int id;
    public String original_language;
    public String original_title;
    public String overview;
    public String release_date;
    public String poster_path;
    public double popularity;
    public String title;
    public boolean video;
    public double vote_average;
    public long vote_count;

    public TmdbMovieItem() {
        adult = false;
        backdrop_path = "";
        genre_ids = new ArrayList<>();
        id = -1;
        original_language = "";
        original_title = "";
        overview = "";
        release_date = "";
        poster_path = "";
        popularity = -1.0;
        title = "";
        video = false;
        vote_average = -1.0;
        vote_count = -1;
    }
}
