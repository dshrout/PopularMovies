package com.example.dshrout.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dshrout.popularmovies.asynctasks.GetDetailsTask;
import com.example.dshrout.popularmovies.asynctasks.GetReviewsTask;
import com.example.dshrout.popularmovies.asynctasks.GetTrailersTask;
import com.example.dshrout.popularmovies.data.PopMoviesContract;
import com.example.dshrout.popularmovies.data.ReviewsItem;
import com.example.dshrout.popularmovies.data.TrailersItem;
import com.example.dshrout.popularmovies.widgets.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


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

    private View mRootView;
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mDate;
    private TextView mRuntime;
    private TextView mRating;
    private TextView mSummary;
    private TextView mReviewsHeading;
    private TextView mTrailersHeading;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mDetailsUri = getArguments().getParcelable(MOVIE_DETAIL_URI);
        }

        mRootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        mTitle = ((TextView) mRootView.findViewById(R.id.moviedetail_title));
        mPoster = ((ImageView) mRootView.findViewById(R.id.moviedetail_poster));
        mDate = ((TextView) mRootView.findViewById(R.id.moviedetail_releasedate));
        mRuntime = ((TextView) mRootView.findViewById(R.id.moviedetail_runtime));
        mRating = ((TextView) mRootView.findViewById(R.id.moviedetail_userrating));
        mSummary = ((TextView) mRootView.findViewById(R.id.moviedetail_summary_content));
        mReviewsHeading = ((TextView) mRootView.findViewById(R.id.moviedetail_reviews_heading));
        mTrailersHeading = ((TextView) mRootView.findViewById(R.id.moviedetail_trailers_heading));

        return mRootView;
    }

    public void populateReviewsList(ArrayList<ReviewsItem> reviews) {
        LinearLayout layout = (LinearLayout) mRootView.findViewById(R.id.moviedetail_reviews_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        int position = 0;
        for(ReviewsItem review: reviews) {
            boolean zebraStripe = (position++ % 2 == 0);
            int color = zebraStripe ? Color.BLACK : Color.parseColor("#222222");
            TextView author = new TextView(getActivity());
            ExpandableTextView content = new ExpandableTextView(getActivity());
            TextView hrule = new TextView(getActivity());

            author.setText(review.author);
            author.setTextColor(Color.WHITE);
            author.setBackgroundColor(color);
            author.setPadding(12, 15, 12, 5);
            layout.addView(author, lp);

            content.setText(review.content);
            content.setTextColor(Color.WHITE);
            content.setBackgroundColor(color);
            content.setPadding(12, 12, 15, 25);
            layout.addView(content, lp);

            hrule.setHeight(2);
            hrule.setBackgroundColor(Color.parseColor("#a1a1a1"));
            hrule.setPadding(12, 12, 15, 15);
            layout.addView(hrule);
        }
    }

    public void populateTrailersList(ArrayList<TrailersItem> trailers) {
        LinearLayout parentLayout = (LinearLayout) mRootView.findViewById(R.id.moviedetail_trailers_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        for(TrailersItem trailer: trailers) {
            LinearLayout childLayout = new LinearLayout(getActivity());
            childLayout.setLayoutParams(lp);
            childLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView icon = new TextView(getActivity());
            TextView text = new TextView(getActivity());

            icon.setText("\u25BA");
            icon.setTextColor(Color.parseColor("#cd201f"));
            icon.setPadding(12, 12, 12, 12);
            //icon.setTextSize(48);
            childLayout.addView(icon, lp);

            //text.setText("https://www.youtube.com/watch?v=" + trailer.key);
            text.setText(trailer.name);
            text.setTextColor(Color.WHITE);
            text.setBackgroundColor(Color.BLACK);
            text.setPadding(12, 12, 12, 12);
            childLayout.addView(text, lp);

            parentLayout.addView(childLayout);
        }

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
            new GetReviewsTask(getActivity(), this).execute(movieId);
            new GetTrailersTask(getActivity(), this).execute(movieId);
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

        mReviewsHeading.setText(R.string.heading_reviews);
        mTrailersHeading.setText(R.string.heading_trailers);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*return*/
    }
}




















