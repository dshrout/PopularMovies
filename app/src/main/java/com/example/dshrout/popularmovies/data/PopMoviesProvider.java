package com.example.dshrout.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.dshrout.popularmovies.data.PopMoviesContract.PostersEntry;
import com.example.dshrout.popularmovies.data.PopMoviesContract.DetailsEntry;
import com.example.dshrout.popularmovies.data.PopMoviesContract.ReviewsEntry;

/*
 * Created by DShrout on 10/28/2015.
 */
public class PopMoviesProvider extends ContentProvider {
    private PopMoviesDbHelper popMoviesDbHelper;

    static final int POSTERS = 100;
    static final int DETAILS = 200;
    static final int DETAILS_WITH_MOVIE_ID = 201;
    static final int REVIEWS = 300;
    static final int REVIEWS_WITH_MOVIE_ID = 301;

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
        matcher.addURI(authority, PopMoviesContract.PATH_REVIEWS + "/movie/#", REVIEWS_WITH_MOVIE_ID);

        return matcher;
    }

    private Cursor getPosters(String[] projection, String selection, String[] selectionArgs, String sortColumn) {
        return  popMoviesDbHelper.getReadableDatabase().query(PostersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortColumn);
    }

    private Cursor getDetails(String[] projection, String selection, String[] selectionArgs, String sortColumn) {
        return  popMoviesDbHelper.getReadableDatabase().query(DetailsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortColumn);
    }

    private Cursor getReviews(String[] projection, String selection, String[] selectionArgs, String sortColumn) {
        return  popMoviesDbHelper.getReadableDatabase().query(ReviewsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortColumn);
    }

    @Nullable
    private Cursor getDetailsByMovieId(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            long movieId = ContentUris.parseId(uri);
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

            if (movieId > 0) {
                queryBuilder.setTables(DetailsEntry.TABLE_NAME);
                queryBuilder.appendWhere(DetailsEntry.COLUMN_MOVIE_ID + " = " + movieId);

                return queryBuilder.query(popMoviesDbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
            } else {
                return null;
            }
        } catch(Exception e) {
            return null;
        }
    }

    @Nullable
    private Cursor getReviewsByMovieId(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            long movieId = ContentUris.parseId(uri);
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

            if (movieId > 0) {
                queryBuilder.setTables(ReviewsEntry.TABLE_NAME);
                queryBuilder.appendWhere(ReviewsEntry.COLUMN_MOVIE_ID + " = " + movieId);

                return queryBuilder.query(popMoviesDbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
            } else {
                return null;
            }
        } catch(Exception e) {
            return null;
        }
    }

    // This method is to assist the testing framework in running smoothly.
    @Override
    @TargetApi(11)
    public void shutdown() {
        popMoviesDbHelper.close();
        super.shutdown();
    }

    /**
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        try {
            popMoviesDbHelper = new PopMoviesDbHelper(getContext());
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
                return PostersEntry.CONTENT_TYPE;
            case DETAILS:
                return DetailsEntry.CONTENT_TYPE;
            case DETAILS_WITH_MOVIE_ID:
                return DetailsEntry.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return ReviewsEntry.CONTENT_TYPE;
            case REVIEWS_WITH_MOVIE_ID:
                return ReviewsEntry.CONTENT_TYPE;
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
     * @param sortBy        How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortBy) {
        Cursor cursor;

        switch (_uriMatcher.match(uri))
        {
            case POSTERS:
                cursor = getPosters(projection, selection, selectionArgs, sortBy);
                break;
            case DETAILS:
                cursor = getDetails(projection, selection, selectionArgs, sortBy);
                break;
            case DETAILS_WITH_MOVIE_ID:
                cursor = getDetailsByMovieId(uri, projection, selection, selectionArgs, sortBy);
                break;
            case REVIEWS:
                cursor = getReviews(projection, selection, selectionArgs, sortBy);
                break;
            case REVIEWS_WITH_MOVIE_ID:
                cursor = getReviewsByMovieId(uri, projection, selection, selectionArgs, sortBy);
                break;
            default:
                throw new UnsupportedOperationException("QUERY: Undefined URI code: " + uri);
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    /**
     * @param uri    The content:// URI of the insertion request. This must not be {@code null}.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The URI for the newly inserted item.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = popMoviesDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (_uriMatcher.match(uri)) {
            case POSTERS: {
                long id = database.insert(PostersEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = PostersEntry.buildPosterUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case DETAILS: {
                long id = database.insert(DetailsEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = DetailsEntry.buildDetailsUri(id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS: {
                long id = database.insert(ReviewsEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ReviewsEntry.buildReviewsUri(id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("INSERT: Undefined URI code: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs
     * @return The number of rows affected.
     * @throws SQLException
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = popMoviesDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        if (selection == null) selection = "1";
        switch(_uriMatcher.match(uri))
        {
            case POSTERS:
                rowsDeleted = db.delete(PostersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DETAILS:
                rowsDeleted = db.delete(DetailsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEWS:
                rowsDeleted = db.delete(ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("DELETE: Undefined URI code: " + uri);
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
        final SQLiteDatabase db = popMoviesDbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        if (selection == null) selection = "1";
        switch(_uriMatcher.match(uri))
        {
            case POSTERS:
                rowsUpdated = db.update(PostersEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case DETAILS_WITH_MOVIE_ID:
                rowsUpdated = db.update(DetailsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEWS_WITH_MOVIE_ID:
                rowsUpdated = db.update(ReviewsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("UPDATE: Undefined URI code: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    /**
     * @param uri    The content:// URI of the insertion request.
     * @param values An array of sets of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The number of values that were inserted.
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        String sql;
        int insertCount = 0;
        SQLiteStatement statement;
        final SQLiteDatabase db = popMoviesDbHelper.getWritableDatabase();


        switch (_uriMatcher.match(uri)) {
            case POSTERS:
                db.delete(PostersEntry.TABLE_NAME, null, null);
                sql = "INSERT OR IGNORE INTO " + PostersEntry.TABLE_NAME + " VALUES (?,?,?);";
                statement = db.compileStatement(sql);
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        statement.clearBindings();
                        statement.bindLong(2, value.getAsLong(PostersEntry.COLUMN_MOVIE_ID));
                        statement.bindString(3, value.getAsString(PostersEntry.COLUMN_POSTER_PATH));
                        if (statement.executeInsert() != -1) {
                            ++insertCount;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            case DETAILS:
                db.delete(DetailsEntry.TABLE_NAME, null, null);
                sql = "INSERT OR IGNORE INTO " + DetailsEntry.TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
                statement = db.compileStatement(sql);
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        statement.clearBindings();
                        statement.bindLong(2, value.getAsLong(DetailsEntry.COLUMN_MOVIE_ID));
                        statement.bindString(3, value.getAsString(DetailsEntry.COLUMN_TITLE));
                        statement.bindString(4, value.getAsString(DetailsEntry.COLUMN_TAGLINE));
                        statement.bindString(5, value.getAsString(DetailsEntry.COLUMN_POSTER_PATH));
                        statement.bindString(6, value.getAsString(DetailsEntry.COLUMN_BACKDROP_PATH));
                        statement.bindString(7, value.getAsString(DetailsEntry.COLUMN_OVERVIEW));
                        statement.bindString(8, value.getAsString(DetailsEntry.COLUMN_RELEASE_DATE));
                        statement.bindString(9, value.getAsString(DetailsEntry.COLUMN_RUNTIME));
                        statement.bindString(10, value.getAsString(DetailsEntry.COLUMN_POPULARITY));
                        statement.bindString(11, value.getAsString(DetailsEntry.COLUMN_VOTE_AVERAGE));
                        statement.bindString(12, value.getAsString(DetailsEntry.COLUMN_VOTE_COUNT));
                        if (statement.executeInsert() != -1) {
                            ++insertCount;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            case REVIEWS:
                sql = "INSERT OR IGNORE INTO " + ReviewsEntry.TABLE_NAME + " VALUES (?,?,?,?,?);";
                statement = db.compileStatement(sql);
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        statement.clearBindings();
                        statement.bindString(2, value.getAsString(ReviewsEntry.COLUMN_REVIEW_ID));
                        statement.bindLong(3, value.getAsLong(ReviewsEntry.COLUMN_MOVIE_ID));
                        statement.bindString(4, value.getAsString(ReviewsEntry.COLUMN_AUTHOR));
                        statement.bindString(5, value.getAsString(ReviewsEntry.COLUMN_CONTENT));
                        if (statement.executeInsert() != -1) {
                            ++insertCount;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
               break;
            default:
                return super.bulkInsert(uri, values);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return insertCount;
    }
}


























