package com.example.dshrout.popularmovies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by DShrout on 2/8/2016.
 */
public class TestPopMoviesContract extends AndroidTestCase {
    private static final int TEST_MOVIE_ID = 550;
    private static final long TEST_ROW_ID = 999;

    public void testBuildPosterUri() {
        Uri posterUri = PopMoviesContract.PostersEntry.buildPosterUri(TEST_ROW_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildPosterUri in the PostersEntry section of PopMoviesContract.", posterUri);
        assertEquals("Error: Row ID not properly appended to the end of the Uri", String.valueOf(TEST_ROW_ID), posterUri.getLastPathSegment());
        assertEquals("Error: Poster Uri doesn't match our expected result", posterUri.toString(), "content://com.example.dshrout.popularmovies.app/posters/" + TEST_ROW_ID);
    }

    public void testBuildDetailsUri() {
        Uri detailsUri = PopMoviesContract.DetailsEntry.buildDetailsUri(TEST_ROW_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildDetailsUri in the DetailsEntry section of PopMoviesContract.", detailsUri);
        assertEquals("Error: Row ID not properly appended to the end of the Uri", String.valueOf(TEST_ROW_ID), detailsUri.getLastPathSegment());
        assertEquals("Error: Details Uri doesn't match our expected result", detailsUri.toString(), "content://com.example.dshrout.popularmovies.app/details/" + TEST_ROW_ID);
    }

    public void testBuildDetailsByMovieIdUri() {
        Uri detailsByMovieIdUri = PopMoviesContract.DetailsEntry.buildDetailsByMovieIdUri(TEST_MOVIE_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildDetailsByMovieIdUri in the DetailsEntry section of PopMoviesContract.", detailsByMovieIdUri);
        assertEquals("Error: Movie ID not properly appended to the end of the Uri", String.valueOf(TEST_MOVIE_ID),  detailsByMovieIdUri.getLastPathSegment());
        assertEquals("Error: DetailsByMovieId Uri doesn't match our expected result", detailsByMovieIdUri.toString(), "content://com.example.dshrout.popularmovies.app/details/movie/" + TEST_MOVIE_ID);
    }

    public void testBuildReviewsUri() {
        Uri reviewsUri = PopMoviesContract.ReviewsEntry.buildReviewsUri(TEST_ROW_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildReviewsUri in the ReviewsEntry section of PopMoviesContract.", reviewsUri);
        assertEquals("Error: Row ID not properly appended to the end of the Uri", String.valueOf(TEST_ROW_ID), reviewsUri.getLastPathSegment());
        assertEquals("Error: Reviews Uri doesn't match our expected result", reviewsUri.toString(), "content://com.example.dshrout.popularmovies.app/reviews/" + TEST_ROW_ID);
    }

    public void testBuildReviewsByMovieIdUri() {
        Uri reviewsByMovieIdUri = PopMoviesContract.ReviewsEntry.buildReviewsByMovieIdUri(TEST_MOVIE_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildReviewsByMovieIdUri in the ReviewsEntry section of PopMoviesContract.", reviewsByMovieIdUri);
        assertEquals("Error: Movie ID not properly appended to the end of the Uri", String.valueOf(TEST_MOVIE_ID), reviewsByMovieIdUri.getLastPathSegment());
        assertEquals("Error: ReviewsByMovieId Uri doesn't match our expected result", reviewsByMovieIdUri.toString(), "content://com.example.dshrout.popularmovies.app/reviews/movie/" + TEST_MOVIE_ID);
    }
}
