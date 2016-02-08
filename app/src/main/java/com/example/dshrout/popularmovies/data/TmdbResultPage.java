package com.example.dshrout.popularmovies.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DShrout on 10/13/2015.
 */
public class TmdbResultPage {
    public long page;
    public List<TmdbMovieItem> results;
    public long total_pages;
    public long total_results;

    public TmdbResultPage() {
        page = -1;
        results = new ArrayList<>();
        total_pages = -1;
        total_results = -1;
    }
}
