package com.example.dshrout.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dshrout.popularmovies.asynctasks.GetDetailsTask;
import com.example.dshrout.popularmovies.data.PopMoviesContract;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DETAILS_LOADER = 1001;
    private View mRootView;

    private static final String[] DETAILS_COLUMNS = {
            PopMoviesContract.DetailsEntry._ID,
            PopMoviesContract.DetailsEntry.COLUMN_MOVIE_ID,
            PopMoviesContract.DetailsEntry.COLUMN_TITLE,
            PopMoviesContract.DetailsEntry.COLUMN_TAGLINE,
            PopMoviesContract.DetailsEntry.COLUMN_POSTER_PATH,
            PopMoviesContract.DetailsEntry.COLUMN_BACKDROP_PATH,
            PopMoviesContract.DetailsEntry.COLUMN_OVERVIEW,
            PopMoviesContract.DetailsEntry.COLUMN_RELEASE_DATE,
            PopMoviesContract.DetailsEntry.COLUMN_RUNTIME,
            PopMoviesContract.DetailsEntry.COLUMN_POPULARITY,
            PopMoviesContract.DetailsEntry.COLUMN_VOTE_AVERAGE,
            PopMoviesContract.DetailsEntry.COLUMN_VOTE_COUNT
    };
    // These indices are tied to DETAILS_COLUMNS.  If DETAILS_COLUMNS changes, these must change.
    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_TAGLINE = 3;
    public static final int COL_POSTER_PATH = 4;
    public static final int COL_BACKDROP_PATH = 5;
    public static final int COL_OVERVIEW = 6;
    public static final int COL_RELEASE_DATE = 7;
    public static final int COL_RUNTIME = 8;
    public static final int COL_POPULARITY = 9;
    public static final int COL_VOTE_AVERAGE = 10;
    public static final int COL_VOTE_COUNT = 11;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        return mRootView;
    }

    private void loadMovieDetails(){
        try {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                long movieId = PopMoviesContract.DetailsEntry.getMovieIdFromUri(intent.getData());
                new GetDetailsTask(getActivity()).execute(movieId);
            }
        } catch (Exception e) {
            Log.e("loadMovieDetails", "Error ", e);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        loadMovieDetails();
        getLoaderManager().initLoader(DETAILS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(getActivity(), intent.getData(), DETAILS_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w342/";
        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }

        TextView title = ((TextView) mRootView.findViewById(R.id.moviedetail_title));
        ImageView poster = ((ImageView) mRootView.findViewById(R.id.moviedetail_poster));
        TextView date = ((TextView) mRootView.findViewById(R.id.moviedetail_releasedate));
        TextView runtime = ((TextView) mRootView.findViewById(R.id.moviedetail_runtime));
        TextView rating = ((TextView) mRootView.findViewById(R.id.moviedetail_userrating));
        TextView summary = ((TextView) mRootView.findViewById(R.id.moviedetail_summary));

        title.setText(cursor.getString(COL_TITLE));

        String posterPath = cursor.getString(COL_POSTER_PATH);
        if (posterPath != null && posterPath.length() > 0) {
            Picasso.with(getActivity()).load(TMDB_IMAGE_PATH + posterPath).into(poster);
        } else {
            poster.setImageResource(R.drawable.no_image_found);
        }

        String releaseDate = cursor.getString(COL_RELEASE_DATE);
        if (releaseDate != null && releaseDate.length() == 8) {
            date.setText(releaseDate.substring(0, 4));
        }

        runtime.setText(String.format(getResources().getString(R.string.format_runtime), Integer.toString(cursor.getInt(COL_RUNTIME)).trim()));
        rating.setText(String.format(getResources().getString(R.string.format_vote_average), cursor.getString(COL_VOTE_AVERAGE).trim()));

        String overview = cursor.getString(COL_OVERVIEW);
        if (overview != null && overview.length() > 0) {
            summary.setText(overview);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*return*/
    }
}




















