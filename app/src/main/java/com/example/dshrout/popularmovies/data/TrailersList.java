package com.example.dshrout.popularmovies.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DShrout on 5/23/2016.
 */
public class TrailersList {
    public long id;
    public List<TrailersItem> results;

    public TrailersList() {
        id = -1;
        results = new ArrayList<>();
    }
}
