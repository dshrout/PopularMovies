package com.example.dshrout.popularmovies.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DShrout on 10/13/2015.
 */
public class PostersList {
    public long page;
    public List<PostersItem> results;
    public long total_pages;
    public long total_results;

    public PostersList() {
        page = -1;
        results = new ArrayList<>();
        total_pages = -1;
        total_results = -1;
    }
}
