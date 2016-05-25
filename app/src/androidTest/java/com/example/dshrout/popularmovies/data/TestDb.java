package com.example.dshrout.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.dshrout.popularmovies.helpers.TestHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by DShrout on 2/15/2016.
 */
public class TestDb extends AndroidTestCase {
    // start each test with a clean slate
    public void setUp() {
        mContext.deleteDatabase(PopMoviesDbHelper.DATABASE_NAME);
    }

    public void testCreateDb() throws Throwable {
        // create a hash set of all the tables we wish to create
        final HashSet<String> tableNames = new HashSet<>();
        tableNames.add(PopMoviesContract.PostersEntry.TABLE_NAME);
        tableNames.add(PopMoviesContract.FavoritesEntry.TABLE_NAME);
        tableNames.add(PopMoviesContract.DetailsEntry.TABLE_NAME);
        tableNames.add(PopMoviesContract.ReviewsEntry.TABLE_NAME);

        // create a hash set of column names for the Poster table
        final HashSet<String> postersColumns = new HashSet<>();
        postersColumns.add(PopMoviesContract.PostersEntry._ID);
        postersColumns.add(PopMoviesContract.PostersEntry.COLUMN_MOVIE_ID);
        postersColumns.add(PopMoviesContract.PostersEntry.COLUMN_POSTER_PATH);

        // create a hash set of column names for the Poster table
        final HashSet<String> favoritesColumns = new HashSet<>();
        favoritesColumns.add(PopMoviesContract.FavoritesEntry._ID);
        favoritesColumns.add(PopMoviesContract.FavoritesEntry.COLUMN_MOVIE_ID);
        favoritesColumns.add(PopMoviesContract.FavoritesEntry.COLUMN_POSTER_PATH);

        // create a hash set of column names for the Details table
        final HashSet<String> detailsColumns = new HashSet<>();
        detailsColumns.add(PopMoviesContract.DetailsEntry._ID);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_MOVIE_ID);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_TITLE);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_TAGLINE);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_POSTER_PATH);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_BACKDROP_PATH);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_OVERVIEW);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_RELEASE_DATE);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_RUNTIME);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_POPULARITY);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_VOTE_AVERAGE);
        detailsColumns.add(PopMoviesContract.DetailsEntry.COLUMN_VOTE_COUNT);

        // create a hash set of column names for the Reviews table
        final HashSet<String> reviewsColumns = new HashSet<>();
        reviewsColumns.add(PopMoviesContract.ReviewsEntry._ID);
        reviewsColumns.add(PopMoviesContract.ReviewsEntry.COLUMN_MOVIE_ID);
        reviewsColumns.add(PopMoviesContract.ReviewsEntry.COLUMN_REVIEW_ID);
        reviewsColumns.add(PopMoviesContract.ReviewsEntry.COLUMN_AUTHOR);
        reviewsColumns.add(PopMoviesContract.ReviewsEntry.COLUMN_CONTENT);

        // delete the existing database
        mContext.deleteDatabase(PopMoviesDbHelper.DATABASE_NAME);

        // TEST: can we create the database?
        SQLiteDatabase db = new PopMoviesDbHelper(mContext).getWritableDatabase();
        assertTrue("ERROR: database was not created!", db.isOpen());

        // TEST: does the database have tables?
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("ERROR: database was created without tables!", cursor.moveToFirst());

        // TEST: does the database have our tables?
        do {
            tableNames.remove(cursor.getString(0));
        } while(cursor.moveToNext());
        assertTrue("ERROR: database was created without all the proper tables!", tableNames.isEmpty());

        // run the column tests for each table
        testTableColumns(db, PopMoviesContract.PostersEntry.TABLE_NAME, postersColumns);
        testTableColumns(db, PopMoviesContract.FavoritesEntry.TABLE_NAME, favoritesColumns);
        testTableColumns(db, PopMoviesContract.DetailsEntry.TABLE_NAME, detailsColumns);
        testTableColumns(db, PopMoviesContract.ReviewsEntry.TABLE_NAME, reviewsColumns);

        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }

    private void testTableColumns(SQLiteDatabase db, String tableName, HashSet<String> expectedColumns) {
        // TEST: can we get table info for the table?
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        assertTrue("ERROR: could not get table_info for " + tableName, cursor.moveToFirst());

        // TEST: does our table have the correct column names?
        int columnIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnIndex);
            expectedColumns.remove(columnName);
        } while (cursor.moveToNext());
        assertTrue("ERROR: " + tableName + " table is missing columns!", expectedColumns.isEmpty());
        cursor.close();
    }

    public void testReadWriteDb() throws Throwable {
        testTableData(TestHelper.createPosterValues(), PopMoviesContract.PostersEntry.TABLE_NAME);
        testTableData(TestHelper.createDetailsValues(), PopMoviesContract.DetailsEntry.TABLE_NAME);
        testTableData(TestHelper.createReviewsValues(), PopMoviesContract.ReviewsEntry.TABLE_NAME);
    }

    private void testTableData(ContentValues testContent, String tableName) {
        // First step: Get reference to writable database
        SQLiteDatabase db = new PopMoviesDbHelper(this.mContext).getWritableDatabase();

        // TEST: can we insert data into the database?
        long rowId = db.insert(tableName, null, testContent);
        assertTrue("Error: Unable to insert record(s) into the " + tableName + " table!", rowId != -1);

        // TEST: can we query the database?
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        assertTrue("Error: No records returned from " + tableName + " table. Read/Write validation failed!", cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        testCurrentRecord(cursor, testContent, tableName);

        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }

    private void testCurrentRecord(Cursor cursor, ContentValues expectedValues, String tableName) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = cursor.getColumnIndex(columnName);
            String expectedValue = entry.getValue().toString();
            String actualValue = cursor.getString(idx);
            assertTrue("Error: Column '" + columnName + "' not found in " + tableName + " table!", idx != -1);
            assertEquals("Error: " + tableName + "." + columnName + " - The actual value '" + actualValue + "' did not match the expected value '" + expectedValue + "'!", expectedValue, actualValue);
        }
    }
}








































