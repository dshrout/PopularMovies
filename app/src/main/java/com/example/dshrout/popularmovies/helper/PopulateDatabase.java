package com.example.dshrout.popularmovies.helper;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dshrout.popularmovies.data.DetailsItem;
import com.example.dshrout.popularmovies.data.PopMoviesContract;
import com.example.dshrout.popularmovies.data.PostersItem;
import com.example.dshrout.popularmovies.data.PostersList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PopulateDatabase {
    private final Context mContext;

    public PopulateDatabase(Context context) {
        mContext = context;
    }

    // get a list of movie posters

    @Nullable
    public void populatePostersTable(String postersData) {
        try {
            ArrayList<ContentValues> postersList = new ArrayList<>();
            List<PostersItem> posters = new Gson().fromJson(postersData, PostersList.class).results;

            for (PostersItem poster : posters) {
                ContentValues posterValues = new ContentValues();
                posterValues.put(PopMoviesContract.PostersEntry.COLUMN_MOVIE_ID, poster.id);
                posterValues.put(PopMoviesContract.PostersEntry.COLUMN_POSTER_PATH, poster.poster_path);
                postersList.add(posterValues);
            }

            if (postersList.size() > 0) {
                mContext.getContentResolver().bulkInsert(PopMoviesContract.PostersEntry.CONTENT_URI, postersList.toArray(new ContentValues[postersList.size()]));
            }
        } catch (Exception e) {
            Log.e("populatePostersTable", "Exception", e);
        }
    }

    @Nullable
    public void populateDetailsTable(String detailsData) {
        try {
            ContentValues[] detailsArray = new ContentValues[1];
            DetailsItem details = new Gson().fromJson(detailsData, DetailsItem.class);
            ContentValues detailValues = new ContentValues();

            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_MOVIE_ID, details.id);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_TITLE, details.title);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_TAGLINE, details.tagline);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_POSTER_PATH, details.poster_path);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_BACKDROP_PATH, details.backdrop_path);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_OVERVIEW, details.overview);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_RELEASE_DATE, details.release_date);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_RUNTIME, details.runtime);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_POPULARITY, details.popularity);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_VOTE_AVERAGE, details.vote_average);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_VOTE_COUNT, details.vote_count);

            detailsArray[0] = detailValues;
            mContext.getContentResolver().bulkInsert(PopMoviesContract.DetailsEntry.CONTENT_URI, detailsArray);
        } catch (Exception e) {
            Log.e("populatePostersTable", "Exception", e);
        }
    }
}
