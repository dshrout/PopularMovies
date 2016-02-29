package com.example.dshrout.popularmovies.data;

import android.content.ContentValues;

/**
 * Created by DShrout on 2/25/2016.
 */
public class TestHelper {
    static final int TEST_MOVIE_ID = 550;

    static ContentValues createPosterValues() {
        ContentValues posterValues = new ContentValues();
        posterValues.put(PopMoviesContract.PosterEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
        posterValues.put(PopMoviesContract.PosterEntry.COLUMN_POSTER_PATH, "/2lECpi35Hnbpa4y46JX0aY3AWTy.jpg");
        posterValues.put(PopMoviesContract.PosterEntry.COLUMN_POPULARITY, 2.50307202280779);
        posterValues.put(PopMoviesContract.PosterEntry.COLUMN_VOTE_AVERAGE, 7.7);
        posterValues.put(PopMoviesContract.PosterEntry.COLUMN_FAVORITE, 1);
        return posterValues;
    }

    static ContentValues createDetailsValues() {
        ContentValues detailsValues = new ContentValues();
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_ADULT, 0);
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_BACKDROP_PATH, "/hNFMawyNDWZKKHU4GYCBz1krsRM.jpg");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_GENRE_IDS, ".18.");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_ORIGINAL_LANGUAGE, "en");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_ORIGINAL_TITLE, "Fight Club");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_OVERVIEW, "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \\\"fight clubs\\\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_RELEASE_DATE, "1999-10-14");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_TITLE, "Fight Club");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_VIDEO, 0);
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_VOTE_COUNT, 3185);
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_RUNTIME, 139);
        return detailsValues;
    }

    static ContentValues createReviewsValues() {
        ContentValues reviewsValues = new ContentValues();
        reviewsValues.put(PopMoviesContract.ReviewsEntry.COLUMN_REVIEW_ID, "5013bc76760ee372cb00253e");
        reviewsValues.put(PopMoviesContract.ReviewsEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
        reviewsValues.put(PopMoviesContract.ReviewsEntry.COLUMN_AUTHOR, "mfreeman");
        reviewsValues.put(PopMoviesContract.ReviewsEntry.COLUMN_CONTENT, "I thought this film rocked!");
        return reviewsValues;
    }
}
