package com.example.dshrout.popularmovies.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.dshrout.popularmovies.data.ReviewsItem;

/**
 * Created by DShrout on 5/20/2016
 */
public class ReviewAdapter extends ArrayAdapter<ReviewsItem> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public ReviewAdapter(Context context, int resource) {
        super(context, resource);
    }
}
