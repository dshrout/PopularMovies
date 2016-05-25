package com.example.dshrout.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines tables and columns for the popular movies database
 */
public class PopMoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.dshrout.popularmovies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POSTERS = "posters";
    public static final String PATH_DETAILS = "details";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_FAVORITES = "favorites";

    // The posters table will be used for the main activity.
    // We'll only fetch what we need to show and sort the posters.
    // We'll need the movie id to pass to the details fragment when the user clicks a poster.
    public static final class PostersEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTERS).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POSTERS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POSTERS;

        public static final String TABLE_NAME = "posters";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static Uri buildPosterUri(long id) {
            // content://com.example.dshrout.popularmovies.app/posters/[id]
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    // The favorites table will be used for the main activity.
    // We'll only fetch what we need to show and sort the posters.
    // We'll need the movie id to pass to the details fragment when the user clicks a poster.
    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static Uri buildFavoritesUri(long id) {
            // content://com.example.dshrout.popularmovies.app/favorites/[id]
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    // The details table will be used for the detail fragment.
    // We won't fetch these until we need them.
    public static final class DetailsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DETAILS).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAILS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAILS;

        public static final String TABLE_NAME = "details";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";

        public static Uri buildDetailsUri(long id) {
            // content://com.example.dshrout.popularmovies.app/details/[id]
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildDetailsByMovieIdUri(int movie_id) {
            // content://com.example.dshrout.popularmovies.app/details/movie/[movie_id]
            return CONTENT_URI.buildUpon()
                    .appendPath("movie")
                    .appendPath(Integer.toString(movie_id))
                    .build();
        }

        public static long getMovieIdFromUri(Uri movieIdUri) {
            return Long.parseLong(movieIdUri.getLastPathSegment());
        }
    }

    // The reviews table will be used for the reviews segment of the detail fragment
    // We'll fetch the reviews when we fetch the details
    public static final class ReviewsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";

        public static Uri buildReviewsUri(long id) {
            // content://com.example.dshrout.popularmovies.app/reviews/[id]
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewsByMovieIdUri(int movie_id) {
            // content://com.example.dshrout.popularmovies.app/reviews/movie/[movie_id]
            return CONTENT_URI.buildUpon()
                    .appendPath("movie")
                    .appendPath(Integer.toString(movie_id))
                    .build();
        }

        public static long getMovieIdFromUri(Uri movieIdUri) {
            return Long.parseLong(movieIdUri.getLastPathSegment());
        }
    }
}
