package com.example.dshrout.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.dshrout.popularmovies.MainFragment;
import com.example.dshrout.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by DShrout on 4/26/2016
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
        View view = LayoutInflater.from(context).inflate(R.layout.moviecard_listitem, parent, false);
        ViewPlaceholder placeholder = new ViewPlaceholder(view);
        view.setTag(placeholder);
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
        ViewPlaceholder placeholder = (ViewPlaceholder)view.getTag();
        String posterPath = cursor.getString(MainFragment.COL_POSTER_PATH);
        if (posterPath != null && !posterPath.isEmpty()) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + posterPath).into(placeholder.posterItem);
        } else {
            placeholder.posterItem.setImageResource(R.drawable.no_image_found);
        }
    }

    // Passing a placeholder class in the tag of an inflated view will reduce the number of calls to findViewById.
    public static class ViewPlaceholder {
        public final ImageView posterItem;

        public ViewPlaceholder(View view) {
            posterItem = (ImageView) view.findViewById(R.id.moviecard_listitem_imageview);
        }
    }
}
