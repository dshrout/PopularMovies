package com.example.dshrout.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dshrout.popularmovies.R;
import com.example.dshrout.popularmovies.data.ReviewsItem;
import com.example.dshrout.popularmovies.widgets.ExpandableTextView;

import java.util.ArrayList;

/**
 * Created by DShrout on 5/20/2016
 */
public class ReviewsAdapter extends ArrayAdapter<ReviewsItem> {
    private final Context mContext;
    private final int mLayoutResourceId;
    private final ArrayList<ReviewsItem> mReviews;

    private int index = 0;

    public ReviewsAdapter(Context context, int resource, ArrayList<ReviewsItem> reviews) {
        super(context, resource, reviews);
        mContext = context;
        mLayoutResourceId = resource;
        mReviews = reviews;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewPlaceholder viewPlaceholder = null;
        boolean zebraStripe = (position % 2 == 0);
        int color = zebraStripe ? Color.BLACK : Color.parseColor("#222222");

        if(view == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            view = inflater.inflate(mLayoutResourceId, parent, false);

            viewPlaceholder = new ViewPlaceholder(view);

            view.setTag(viewPlaceholder);
        }
        else
        {
            viewPlaceholder = (ViewPlaceholder)view.getTag();
        }

        ReviewsItem review = mReviews.get(position);

        viewPlaceholder.author.setText(review.author);
        viewPlaceholder.content.setText(review.content);

        view.setBackgroundColor(color);
        viewPlaceholder.author.setBackgroundColor(color);
        viewPlaceholder.content.setBackgroundColor(color);

        return view;
    }

    public static class ViewPlaceholder
    {
        public final TextView author;
        public final ExpandableTextView content;

        public ViewPlaceholder(View view) {
            author = (TextView)view.findViewById(R.id.moviedetail_review_author);
            content = (ExpandableTextView)view.findViewById(R.id.moviedetail_summary_content);
        }
    }}
