package com.example.dshrout.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.dshrout.popularmovies.adapters.PostersCursorAdapter;
import com.example.dshrout.popularmovies.asynctasks.GePostersTask;
import com.example.dshrout.popularmovies.data.PopMoviesContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int POPMOVIES_LOADER = 1000;
    private PostersCursorAdapter mPostersCursorAdapter;

    private static final String[] POSTERS_COLUMNS = {
        PopMoviesContract.PostersEntry._ID,
        PopMoviesContract.PostersEntry.COLUMN_MOVIE_ID,
        PopMoviesContract.PostersEntry.COLUMN_POSTER_PATH
    };
    // These indices are tied to POSTERS_COLUMNS.  If POSTERS_COLUMNS changes, these must change.
    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_POSTER_PATH = 2;

    public MainFragment() {
    }

    /**
     * A callback interface that all activities containing this fragment must implement.
     * This mechanism allows activities to be notified of item selections.
     */
    public interface Callback {
        public void onItemSelected(Uri detailsUri);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (id == R.id.action_popular) {
            sharedPrefs.edit().putString(getActivity().getString(R.string.pref_sortby_key), "popularity").apply();
            updateMovieCards();
            return true;
        } else if (id == R.id.action_user_votes) {
            sharedPrefs.edit().putString(getActivity().getString(R.string.pref_sortby_key), "vote_average").apply();
            updateMovieCards();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Create a list to hold the movie cards then update it
        mPostersCursorAdapter = new PostersCursorAdapter(getActivity(), null, 0);
        //updateMovieCards(rootView);

        // attach adapter to view
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mPostersCursorAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity()).onItemSelected(PopMoviesContract.DetailsEntry.buildDetailsByMovieIdUri(cursor.getInt(COL_MOVIE_ID)));
//                    Intent detailsIntent = new Intent(getActivity(), MovieDetailsActivity.class)
//                            .setData(PopMoviesContract.DetailsEntry.buildDetailsByMovieIdUri(cursor.getInt(COL_MOVIE_ID)));
//                    startActivity(detailsIntent);
                }
            }
        });

        updateMovieCards();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(POPMOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateMovieCards(){
        new GePostersTask(getActivity()).execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // get which category to sort by from the user settings
        String sortBy = sharedPrefs.getString(getActivity().getString(R.string.pref_sortby_key), getActivity().getString(R.string.pref_sortby_default));
        // build the URI we'll query with
        Uri postersUri = PopMoviesContract.PostersEntry.CONTENT_URI;

        return new CursorLoader(getActivity(), postersUri, POSTERS_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mPostersCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPostersCursorAdapter.swapCursor(null);
    }
}
