package com.example.dshrout.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/*
 * Created by DShrout on 10/28/2015.
 */
public class PopMoviesProvider extends ContentProvider {
    private PopMoviesDbHelper mOpenHelper;

    static final int POSTERS = 100;
    static final int DETAILS = 200;
    static final int DETAILS_WITH_MOVIE_ID = 201;
    static final int REVIEWS = 300;
    static final int REVIEW_WITH_MOVIE_ID = 301;

    private static final UriMatcher _uriMatcher = buildUriMatcher();
    static UriMatcher buildUriMatcher() {
        // initialize the matcher to NO_MATCH.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopMoviesContract.CONTENT_AUTHORITY;

        // Posters
        matcher.addURI(authority, PopMoviesContract.PATH_POSTERS, POSTERS);

        // Details
        matcher.addURI(authority, PopMoviesContract.PATH_DETAILS, DETAILS);
        matcher.addURI(authority, PopMoviesContract.PATH_DETAILS + "/movie/#", DETAILS_WITH_MOVIE_ID);

        // Reviews
        matcher.addURI(authority, PopMoviesContract.PATH_REVIEWS, REVIEWS);
        matcher.addURI(authority, PopMoviesContract.PATH_REVIEWS + "/movie/#", REVIEW_WITH_MOVIE_ID);

        return matcher;
    }

    /**
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        try {
            mOpenHelper = new PopMoviesDbHelper(getContext());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    @Override
    public String getType(Uri uri) {
        int match = _uriMatcher.match(uri);
        switch (match) {
            case POSTERS:
                return PopMoviesContract.PosterEntry.CONTENT_TYPE;
            case DETAILS_WITH_MOVIE_ID:
                return PopMoviesContract.DetailsEntry.CONTENT_ITEM_TYPE;
            case REVIEW_WITH_MOVIE_ID:
                return PopMoviesContract.ReviewsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Undefined URI code: " + uri);
        }
    }

    /**
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor results;
        switch (_uriMatcher.match(uri))
        {
            case POSTERS:
                results = mOpenHelper.getReadableDatabase().query(PopMoviesContract.PosterEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DETAILS_WITH_MOVIE_ID:
                results = mOpenHelper.getReadableDatabase().query(PopMoviesContract.DetailsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case REVIEW_WITH_MOVIE_ID:
                results = mOpenHelper.getReadableDatabase().query(PopMoviesContract.ReviewsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Undefined URI code: " + uri);
        }

        results.setNotificationUri(getContext().getContentResolver(), uri);
        return results;
    }

    /**
     * @param uri    The content:// URI of the insertion request. This must not be {@code null}.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The URI for the newly inserted item.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        Uri returnUri = null;

        switch (_uriMatcher.match(uri)) {
            case POSTERS: {
                long id = database.insert(PopMoviesContract.PosterEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = PopMoviesContract.PosterEntry.buildPosterUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case DETAILS_WITH_MOVIE_ID: {
                long id = database.insert(PopMoviesContract.DetailsEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = PopMoviesContract.DetailsEntry.buildDetailsUri(id);
/*
                    long movieId = Long.parseLong(
                            uri.getPathSegments().get(PopMoviesContract.PATH_DETAILS));
                    returnUri = PopMoviesContract.DetailsEntry.buildDetailsByMovieIdUri(movieId);
*/
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW_WITH_MOVIE_ID: {
                long id = database.insert(PopMoviesContract.ReviewsEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = PopMoviesContract.ReviewsEntry.buildReviewsUri(id);
/*
                    long movieId = Long.parseLong(
                            uri.getPathSegments().get(PopMoviesContract.PATH_VIDEO_MOVIE_ID_INDEX));
                    returnUri = PopMoviesContract.ReviewsEntry.buildUriByMovieId(movieId);
*/
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Undefined URI code: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;    }

    /**
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs
     * @return The number of rows affected.
     * @throws SQLException
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted = 0;
        if (selection == null) selection = "1";
        switch(_uriMatcher.match(uri))
        {
            case POSTERS:
                rowsDeleted = db.delete(PopMoviesContract.PosterEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DETAILS:
                rowsDeleted = db.delete(PopMoviesContract.DetailsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEWS:
                rowsDeleted = db.delete(PopMoviesContract.ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Undefined URI code: " + uri);
        }

        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    /**
     * @param uri           The URI to query. This can potentially have a record ID if this
     *                      is an update request for a specific record.
     * @param values        A set of column_name/value pairs to update in the database.
     *                      This must not be {@code null}.
     * @param selection     An optional filter to match rows to update.
     * @param selectionArgs
     * @return the number of rows affected.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdateed = 0;
        if (selection == null) selection = "1";
        switch(_uriMatcher.match(uri))
        {
            case POSTERS:
                rowsUpdateed = db.update(PopMoviesContract.PosterEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case DETAILS:
                rowsUpdateed = db.update(PopMoviesContract.DetailsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEWS:
                rowsUpdateed = db.update(PopMoviesContract.ReviewsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Undefined URI code: " + uri);
        }

        if (rowsUpdateed != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdateed;
    }
}
