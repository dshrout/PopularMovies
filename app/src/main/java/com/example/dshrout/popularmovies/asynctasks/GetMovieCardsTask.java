package com.example.dshrout.popularmovies.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.example.dshrout.popularmovies.ImageAdapter;
import com.example.dshrout.popularmovies.R;
import com.example.dshrout.popularmovies.data.PopMoviesContract;
import com.example.dshrout.popularmovies.helper.MovieCard;
import com.example.dshrout.popularmovies.helper.PopularMovies;

import java.util.ArrayList;

/**
 * Created by DShrout on 4/13/2016.
 */
public class GetMovieCardsTask extends AsyncTask<Void, Void, ArrayList<MovieCard>> {
    private View mRootView;
    private Context mContext;
    private ImageAdapter mMovieCardsAdapter;

    public GetMovieCardsTask(Context context, View rootView, ImageAdapter imageAdapter) {
        mRootView = rootView;
        mContext = context;
        mMovieCardsAdapter = imageAdapter;
    }

    private void setViews() {
        TextView textView = (TextView) mRootView.findViewById(R.id.empty);
        GridView gridView = (GridView) mRootView.findViewById(R.id.gridview_movies);
        if (mMovieCardsAdapter.getCount() > 1) {
            gridView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            gridView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private boolean NetworkAvailable() {
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    @Override
    protected void onPostExecute(ArrayList<MovieCard> movies) {
        super.onPostExecute(movies);
        mMovieCardsAdapter.clear();
        mMovieCardsAdapter.addAll(movies);
        mMovieCardsAdapter.notifyDataSetChanged();
        setViews();
    }

    @Override
    protected ArrayList<MovieCard> doInBackground(Void... params) {
        // TODO: Get data from database. If db is empty, and network is available, query tmdb and populate database
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortBy = sharedPrefs.getString(mContext.getString(R.string.pref_sortby_key), mContext.getString(R.string.pref_sortby_default));
        String sortOrder = sharedPrefs.getString(mContext.getString(R.string.pref_sortorder_key), mContext.getString(R.string.pref_sortorder_default));

        Cursor cursor;
        Context context = mContext.getApplicationContext();
        cursor = mContext.getContentResolver().query(PopMoviesContract.PostersEntry.CONTENT_URI, null, null, null, sortBy);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            cursor.close();
            return new ArrayList<>();
        } else if (NetworkAvailable()) {
            PopularMovies popMovies = new PopularMovies();
            return popMovies.GetMovies(sortBy, sortOrder);
        } else {
            return new ArrayList<>();
        }
    }
}
