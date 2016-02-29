package com.example.dshrout.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

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
        tableNames.add(PopMoviesContract.PosterEntry.TABLE_NAME);
        tableNames.add(PopMoviesContract.DetailsEntry.TABLE_NAME);
        tableNames.add(PopMoviesContract.ReviewsEntry.TABLE_NAME);

        // create a hash set of column names for the Poster table
        final HashSet<String> posterColunms = new HashSet<>();
        posterColunms.add(PopMoviesContract.PosterEntry._ID);
        posterColunms.add(PopMoviesContract.PosterEntry.COLUMN_MOVIE_ID);
        posterColunms.add(PopMoviesContract.PosterEntry.COLUMN_POSTER_PATH);
        posterColunms.add(PopMoviesContract.PosterEntry.COLUMN_POPULARITY);
        posterColunms.add(PopMoviesContract.PosterEntry.COLUMN_VOTE_AVERAGE);
        posterColunms.add(PopMoviesContract.PosterEntry.COLUMN_FAVORITE);

        // create a hash set of column names for the Details table
        final HashSet<String> detailsColunms = new HashSet<>();
        detailsColunms.add(PopMoviesContract.DetailsEntry._ID);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_MOVIE_ID);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_ADULT);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_BACKDROP_PATH);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_GENRE_IDS);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_ORIGINAL_LANGUAGE);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_ORIGINAL_TITLE);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_OVERVIEW);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_RELEASE_DATE);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_TITLE);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_VIDEO);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_VOTE_COUNT);
        detailsColunms.add(PopMoviesContract.DetailsEntry.COLUMN_RUNTIME);

        // create a hash set of column names for the Reviews table
        final HashSet<String> reviewsColunms = new HashSet<>();
        reviewsColunms.add(PopMoviesContract.ReviewsEntry._ID);
        reviewsColunms.add(PopMoviesContract.ReviewsEntry.COLUMN_MOVIE_ID);
        reviewsColunms.add(PopMoviesContract.ReviewsEntry.COLUMN_REVIEW_ID);
        reviewsColunms.add(PopMoviesContract.ReviewsEntry.COLUMN_AUTHOR);
        reviewsColunms.add(PopMoviesContract.ReviewsEntry.COLUMN_CONTENT);

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
        testTableColumns(db, PopMoviesContract.PosterEntry.TABLE_NAME, posterColunms);
        testTableColumns(db, PopMoviesContract.DetailsEntry.TABLE_NAME, detailsColunms);
        testTableColumns(db, PopMoviesContract.ReviewsEntry.TABLE_NAME, reviewsColunms);
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
    }

    public void testReadWriteDb() throws Throwable {
        testTableData(TestHelper.createPosterValues(), PopMoviesContract.PosterEntry.TABLE_NAME);
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








































