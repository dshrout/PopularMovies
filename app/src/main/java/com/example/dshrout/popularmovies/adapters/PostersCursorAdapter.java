package com.example.dshrout.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.example.dshrout.popularmovies.R;

/**
 * Created by DShrout on 4/26/2016.
 */
public class PostersCursorAdapter extends CursorAdapter {

    public PostersCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = (viewType > 0) ? R.layout.moviecard_listitem : R.layout.moviecard_listitem;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
//        MovieViewHolder fvh = new MovieViewHolder(view);
//        view.setTag(fvh);
        return view;
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    /*
        Holder class to cache the child views of a list item.
        Adding this to the tag of an inflated view will prevent unnecessary findViewById calls for recycled views.
     */
//    public static class MovieViewHolder {
//        public final ImageView iconView;
//        public final TextView dateView;
//        public final TextView forecastView;
//        public final TextView highView;
//        public final TextView lowView;
//
//        public MovieViewHolder(View view) {
//            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
//            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
//            forecastView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
//            highView = (TextView) view.findViewById(R.id.list_item_high_textview);
//            lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
//        }
}
