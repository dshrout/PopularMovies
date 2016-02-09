package com.example.dshrout.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by DShrout on 2/8/2016.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String TEST_MOVIE_ID = "23654";

    private static final Uri TEST_POSTERS_DIR = PopMoviesContract.PosterEntry.CONTENT_URI;
    private static final Uri TEST_DETAILS_DIR = PopMoviesContract.DetailsEntry.CONTENT_URI;
    private static final Uri TEST_DETAILS_WITH_MOVIE_ID = PopMoviesContract.DetailsEntry.buildDetailsByMovieIdUri(TEST_MOVIE_ID);
    private static final Uri TEST_REVIEWS_DIR = PopMoviesContract.ReviewsEntry.CONTENT_URI;
    private static final Uri TEST_REVIEW_WITH_MOVIE_ID = PopMoviesContract.ReviewsEntry.buildReviewsByMovieIdUri(TEST_MOVIE_ID);

    public void testUriMatcher() {
        UriMatcher testMatcher = PopMoviesProvider.buildUriMatcher();

        assertEquals("ERROR: The POSTER URI was matched incorrectly.", testMatcher.match(TEST_POSTERS_DIR), PopMoviesProvider.POSTERS);
        assertEquals("ERROR: The POSTER URI was matched incorrectly.", testMatcher.match(TEST_DETAILS_DIR), PopMoviesProvider.DETAILS);
        assertEquals("ERROR: The POSTER URI was matched incorrectly.", testMatcher.match(TEST_DETAILS_WITH_MOVIE_ID), PopMoviesProvider.DETAILS_WITH_MOVIE_ID);
        assertEquals("ERROR: The POSTER URI was matched incorrectly.", testMatcher.match(TEST_REVIEWS_DIR), PopMoviesProvider.REVIEWS);
        assertEquals("ERROR: The POSTER URI was matched incorrectly.", testMatcher.match(TEST_REVIEW_WITH_MOVIE_ID), PopMoviesProvider.REVIEW_WITH_MOVIE_ID);
    }
}
