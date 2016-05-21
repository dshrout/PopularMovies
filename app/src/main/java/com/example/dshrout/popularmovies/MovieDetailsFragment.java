package com.example.dshrout.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dshrout.popularmovies.adapters.ReviewsAdapter;
import com.example.dshrout.popularmovies.asynctasks.GetDetailsTask;
import com.example.dshrout.popularmovies.asynctasks.GetReviewsTask;
import com.example.dshrout.popularmovies.data.PopMoviesContract;
import com.example.dshrout.popularmovies.data.ReviewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DETAILS_LOADER = 1001;
    private Uri mDetailsUri;
    static final String MOVIE_DETAIL_URI = "URI";

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

    private TextView mTitle;
    private ImageView mPoster;
    private TextView mDate;
    private TextView mRuntime;
    private TextView mRating;
    private TextView mSummary;
    private TextView mReviewsHeading;

    private ArrayAdapter<ReviewsItem> mReviewsAdapter;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mDetailsUri = getArguments().getParcelable(MOVIE_DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        mTitle = ((TextView) rootView.findViewById(R.id.moviedetail_title));
        mPoster = ((ImageView) rootView.findViewById(R.id.moviedetail_poster));
        mDate = ((TextView) rootView.findViewById(R.id.moviedetail_releasedate));
        mRuntime = ((TextView) rootView.findViewById(R.id.moviedetail_runtime));
        mRating = ((TextView) rootView.findViewById(R.id.moviedetail_userrating));
        mSummary = ((TextView) rootView.findViewById(R.id.moviedetail_summary_content));
        mReviewsHeading = ((TextView) rootView.findViewById(R.id.moviedetail_reviews_heading));

        // The ArrayAdapter will take data from a source and use it to populate the ListView it's attached to.
        mReviewsAdapter = new ReviewsAdapter(getActivity(), R.layout.reviews_listitem, new ArrayList<ReviewsItem>());

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_reviews);
        listView.setAdapter(mReviewsAdapter);


        return rootView;
    }

    private void loadMovieDetails(){
        try {
            if (mDetailsUri == null) {
                Intent intent = getActivity().getIntent();
                if (intent != null && intent.getData() != null) {
                    mDetailsUri = intent.getData();
                }
            }

            long movieId = PopMoviesContract.DetailsEntry.getMovieIdFromUri(mDetailsUri);
            new GetDetailsTask(getActivity()).execute(movieId);
            new GetReviewsTask(getActivity(), mReviewsAdapter).execute(movieId);
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
        return mDetailsUri == null ? null : new CursorLoader(getActivity(), mDetailsUri, DETAILS_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }

        String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w342/";

        mTitle.setText(cursor.getString(COL_TITLE));

        String posterPath = cursor.getString(COL_POSTER_PATH);
        if (posterPath != null && posterPath.length() > 0) {
            Picasso.with(getActivity()).load(TMDB_IMAGE_PATH + posterPath).into(mPoster);
        } else {
            mPoster.setImageResource(R.drawable.no_image_found);
        }

        String releaseDate = cursor.getString(COL_RELEASE_DATE);
        if (releaseDate != null && releaseDate.length() == 8) {
            mDate.setText(releaseDate.substring(0, 4));
        }

        mRuntime.setText(String.format(getResources().getString(R.string.format_runtime), Integer.toString(cursor.getInt(COL_RUNTIME)).trim()));
        mRating.setText(String.format(getResources().getString(R.string.format_vote_average), cursor.getString(COL_VOTE_AVERAGE).trim()));

        String overview = cursor.getString(COL_OVERVIEW);
        if (overview != null && overview.length() > 0) {
            mSummary.setText(overview);
        }

        mReviewsHeading.setText("REVIEWS:");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*return*/
    }
}




















