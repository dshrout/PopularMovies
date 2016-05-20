package com.example.dshrout.popularmovies.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DShrout on 5/20/2016.
 */
public class ReviewsList {
    public long id;
    public long page;
    public List<ReviewsItem> results;
    public long total_pages;
    public long total_results;

    public ReviewsList() {
        id = -1;
        page = -1;
        results = new ArrayList<>();
        total_pages = -1;
        total_results = -1;
    }
}
