package com.example.dshrout.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;

import com.example.dshrout.popularmovies.data.PopMoviesContract.DetailsEntry;
import com.example.dshrout.popularmovies.data.PopMoviesContract.PostersEntry;
import com.example.dshrout.popularmovies.data.PopMoviesContract.ReviewsEntry;
import com.example.dshrout.popularmovies.helpers.TestHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by DShrout on 3/2/2016.
 */
public class TestProvider extends AndroidTestCase {
    private static final int TEST_MOVIE_ID = 550;
    private static final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    private void deleteAllTableData() {
        TestHelper.deleteTableData(mContext, PostersEntry.TABLE_NAME);
        TestHelper.deleteTableData(mContext, DetailsEntry.TABLE_NAME);
        TestHelper.deleteTableData(mContext, ReviewsEntry.TABLE_NAME);
    }

    private void validateQueryResults(Cursor cursor, String tableName, ContentValues expectedValues) {
        // TEST: did we get the data we expected to get from the query?
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = cursor.getColumnIndex(columnName);
            String expectedValue = entry.getValue().toString();
            String actualValue = cursor.getString(idx);
            assertTrue("Error: Column '" + columnName + "' not found in " + tableName + " table!", idx != -1);
            assertEquals("Error: " + tableName + "." + columnName + " - The actual value '" + actualValue +
                    "' did not match the expected value '" + expectedValue + "'!", expectedValue, actualValue);
        }
    }

    private Map<String, ContentValues[]> createBulkContent(int rowCount) {
        Map<String, ContentValues[]> bulkContent = new HashMap<>();
        ContentValues[] posterList = new ContentValues[rowCount];
        ContentValues[] detailsList = new ContentValues[rowCount];
        ContentValues[] reviewsList = new ContentValues[rowCount];

        for (int i = 0; i < rowCount; ++i) {
            ContentValues posterContent = TestHelper.createPosterValues();
            posterContent.put(PostersEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID + i);
            posterList[i] = posterContent;

            ContentValues detailsContent = TestHelper.createDetailsValues();
            detailsContent.put(DetailsEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID + i);
            detailsContent.put(DetailsEntry.COLUMN_VOTE_COUNT, i);
            detailsList[i] = detailsContent;

            ContentValues reviewsContent = TestHelper.createReviewsValues();
            reviewsContent.put(ReviewsEntry.COLUMN_REVIEW_ID, java.util.UUID.randomUUID().toString());
            reviewsContent.put(ReviewsEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID + i);
            reviewsContent.put(ReviewsEntry.COLUMN_AUTHOR, "mfreeman_" + String.valueOf(i));
            reviewsList[i] = reviewsContent;
        }

        bulkContent.put(PostersEntry.TABLE_NAME, posterList);
        bulkContent.put(DetailsEntry.TABLE_NAME, detailsList);
        bulkContent.put(ReviewsEntry.TABLE_NAME, reviewsList);
        return bulkContent;
    }

    // setUp is ran prior to each individual test in this module
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllTableData();
    }

    public void testProviderRegistry() {
        // TEST: is the PopMoviesProvider registered correctly?
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(), PopMoviesProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertEquals("Error: PopMoviesProvider registered with authority: " + providerInfo.authority +
                         " instead of authority: " + PopMoviesContract.CONTENT_AUTHORITY, providerInfo.authority, PopMoviesContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: PopMoviesProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    public void testGetType() {
        String type;

        // TEST: does the content provider return the correct type for the Posters uri?
        type = mContext.getContentResolver().getType(PostersEntry.CONTENT_URI);
        assertEquals("Error: the PostersEntry CONTENT_URI with row Id should return PostersEntry.CONTENT_TYPE", PostersEntry.CONTENT_TYPE, type);

        // TEST: does the content provider return the correct type for the Details by Movie Id uri?
        type = mContext.getContentResolver().getType(DetailsEntry.buildDetailsByMovieIdUri(TEST_MOVIE_ID));
        assertEquals("Error: the DetailsEntry CONTENT_URI with Movie Id should return DetailsEntry.CONTENT_ITEM_TYPE", DetailsEntry.CONTENT_ITEM_TYPE, type);

        // TEST: does the content provider return the correct type for the Reviews by Movie Id uri?
        type = mContext.getContentResolver().getType(ReviewsEntry.buildReviewsByMovieIdUri(TEST_MOVIE_ID));
        assertEquals("Error: the ReviewsEntry CONTENT_URI with Movie Id should return ReviewsEntry.CONTENT_TYPE", ReviewsEntry.CONTENT_TYPE, type);
    }

    public void testProviderQuery() {
        Cursor cursor;
        ContentValues content;
        boolean insertResult;
        boolean testNotificationUri = (Build.VERSION.SDK_INT >= 19);

        // TEST: can we insert data into POSTERS table?
        content = TestHelper.createPosterValues();
        insertResult = TestHelper.insertTableData(mContext, PostersEntry.TABLE_NAME, content);
        assertTrue("Failed to insert data into POSTERS table", insertResult);
        // TEST: can we query the POSTERS table via the provider?
        cursor = mContext.getContentResolver().query(PostersEntry.CONTENT_URI, null, null, null, null);
        assertTrue("Empty cursor returned when querying POSTERS table", cursor.moveToFirst());
        validateQueryResults(cursor, PostersEntry.TABLE_NAME, content);
        if (testNotificationUri) {
            assertEquals("ERROR: Posters query did not properly set the Notification Uri", cursor.getNotificationUri(), PostersEntry.CONTENT_URI);
        }
        cursor.close();


        // TEST: can we insert data into DETAILS table?
        content = TestHelper.createDetailsValues();
        insertResult = TestHelper.insertTableData(mContext, DetailsEntry.TABLE_NAME, content);
        assertTrue("Failed to insert data into DETAILS table", insertResult);
        // TEST: can we query the DETAILS table via the provider?
        cursor = mContext.getContentResolver().query(DetailsEntry.buildDetailsByMovieIdUri(TEST_MOVIE_ID), null, null, null, null);
        assertTrue("Empty cursor returned when querying DETAILS table", cursor.moveToFirst());
        validateQueryResults(cursor, DetailsEntry.TABLE_NAME, content);
        if (testNotificationUri) {
            assertEquals("ERROR: Details with Movie Id query did not properly set the Notification Uri", cursor.getNotificationUri(), DetailsEntry.buildDetailsByMovieIdUri(TEST_MOVIE_ID));
        }
        cursor.close();


        // TEST: can we insert data into REVIEWS table?
        content = TestHelper.createReviewsValues();
        insertResult = TestHelper.insertTableData(mContext, ReviewsEntry.TABLE_NAME, content);
        assertTrue("Failed to insert data into REVIEWS table", insertResult);
        // TEST: can we query the REVIEWS table via the provider?
        cursor = mContext.getContentResolver().query(ReviewsEntry.buildReviewsByMovieIdUri(TEST_MOVIE_ID), null, null, null, null);
        assertTrue("Empty cursor returned when querying REVIEWS table", cursor.moveToFirst());
        validateQueryResults(cursor, ReviewsEntry.TABLE_NAME, content);
        if (testNotificationUri) {
            assertEquals("ERROR: Reviews with Movie Id query did not properly set the Notification Uri", cursor.getNotificationUri(), ReviewsEntry.buildReviewsByMovieIdUri(TEST_MOVIE_ID));
        }
        cursor.close();
    }

    public void testProviderUpdate() {
        Uri uri;
        int count;
        long rowId;
        Cursor cursor;
        ContentValues content;
        TestHelper.TestContentObserver tco = TestHelper.getTestContentObserver();

        // start with a clean slate
        deleteAllTableData();

        /* Posters Table */
        // create content values and insert into database
        content = TestHelper.createPosterValues();
        uri = mContext.getContentResolver().insert(PostersEntry.CONTENT_URI, content);
        rowId = ContentUris.parseId(uri);
        // TEST: did we insert data correctly?
        assertTrue("ERROR: Setup of POSTER table, for Update test, failed!", rowId != -1);

        // register a content observer so we can test if our Update fires notifyChange correctly
        mContext.getContentResolver().registerContentObserver(PostersEntry.CONTENT_URI, true, tco);

        // modify the content values and update the table
        count = mContext.getContentResolver().update(PostersEntry.CONTENT_URI, content, PostersEntry.COLUMN_MOVIE_ID + " = ?", new String[] { Long.toString(TEST_MOVIE_ID)});
        // TEST: were we able to update the table?
        assertEquals("ERROR: update of POSTERS table failed", 1, count);

        // while not an actual TEST, the app will fail here if our Provider isn't calling notifyChange.
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // query the POSTERS table and verify the new results
        cursor = mContext.getContentResolver().query(PostersEntry.CONTENT_URI, null, PostersEntry.COLUMN_MOVIE_ID + " = " + TEST_MOVIE_ID, null, null);
        assertTrue("Empty cursor returned when querying POSTERS table", cursor.moveToFirst());
        validateQueryResults(cursor, PostersEntry.TABLE_NAME, content);
        cursor.close();

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------

        /* Details Table */
        // create content values and insert into database
        content = TestHelper.createDetailsValues();
        uri = mContext.getContentResolver().insert(DetailsEntry.CONTENT_URI, content);
        rowId = ContentUris.parseId(uri);
        // TEST: did we insert data correctly?
        assertTrue("ERROR: Setup of DETAILS table, for Update test, failed!", rowId != -1);

        // register a content observer so we can test if our Update fires notifyChange correctly
        mContext.getContentResolver().registerContentObserver(DetailsEntry.buildDetailsByMovieIdUri(TEST_MOVIE_ID), true, tco);

        // modify the content values and update the table
        content.put(DetailsEntry.COLUMN_VOTE_COUNT, 3186);
        count = mContext.getContentResolver().update(DetailsEntry.buildDetailsByMovieIdUri(TEST_MOVIE_ID), content, DetailsEntry.COLUMN_MOVIE_ID + " = ?", new String[]{Long.toString(TEST_MOVIE_ID)});
        // TEST: were we able to update the table?
        assertEquals("ERROR: update of DETAILS table failed", 1, count);

        // while not an actual TEST, the app will fail here if our Provider isn't calling notifyChange.
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // query the DETAILS table and verify the new results
        cursor = mContext.getContentResolver().query(DetailsEntry.buildDetailsByMovieIdUri(TEST_MOVIE_ID), null, DetailsEntry.COLUMN_MOVIE_ID + " = " + TEST_MOVIE_ID, null, null);
        assertTrue("Empty cursor returned when querying DETAILS table", cursor.moveToFirst());
        validateQueryResults(cursor, DetailsEntry.TABLE_NAME, content);
        cursor.close();

        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------

        /* Reviews Table */
        // create content values and insert into database
        content = TestHelper.createReviewsValues();
        uri = mContext.getContentResolver().insert(ReviewsEntry.CONTENT_URI, content);
        rowId = ContentUris.parseId(uri);
        // TEST: did we insert data correctly?
        assertTrue("ERROR: Setup of REVIEWS table, for Update test, failed!", rowId != -1);

        // register a content observer so we can test if our Update fires notifyChange correctly
        mContext.getContentResolver().registerContentObserver(ReviewsEntry.buildReviewsByMovieIdUri(TEST_MOVIE_ID), true, tco);

        // modify the content values and update the table
        content.put(ReviewsEntry.COLUMN_CONTENT, "This movie is awesome!");
        count = mContext.getContentResolver().update(ReviewsEntry.buildReviewsByMovieIdUri(TEST_MOVIE_ID), content, ReviewsEntry.COLUMN_MOVIE_ID + " = ?", new String[] { Long.toString(TEST_MOVIE_ID)});
        // TEST: were we able to update the table?
        assertEquals("ERROR: update of REVIEWS table failed", 1, count);

        // while not an actual TEST, the app will fail here if our Provider isn't calling notifyChange.
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // query the REVIEWS table and verify the new results
        cursor = mContext.getContentResolver().query(ReviewsEntry.buildReviewsByMovieIdUri(TEST_MOVIE_ID), null, ReviewsEntry.COLUMN_MOVIE_ID + " = " + TEST_MOVIE_ID, null, null);
        assertTrue("Empty cursor returned when querying REVIEWS table", cursor.moveToFirst());
        validateQueryResults(cursor, ReviewsEntry.TABLE_NAME, content);
        cursor.close();
    }

    public void testDeleteAllRecords() {
        Cursor cursor;
        TestHelper.TestContentObserver tco = TestHelper.getTestContentObserver();


        // TEST: can we delete all records from the Poster table via the Provider?
        TestHelper.insertTableData(mContext, PostersEntry.TABLE_NAME, TestHelper.createPosterValues());
        mContext.getContentResolver().registerContentObserver(PostersEntry.CONTENT_URI, true, tco);
        mContext.getContentResolver().delete(PostersEntry.CONTENT_URI, null, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        cursor = mContext.getContentResolver().query(PostersEntry.CONTENT_URI, null, null, null, null);
        assertEquals("ERROR: Provider could not delete all records from the Poster table.", 0, cursor.getCount());
        cursor.close();

        // TEST: can we delete all records from the Details table via the Provider?
        TestHelper.insertTableData(mContext, PostersEntry.TABLE_NAME, TestHelper.createDetailsValues());
        mContext.getContentResolver().registerContentObserver(DetailsEntry.CONTENT_URI, true, tco);
        mContext.getContentResolver().delete(DetailsEntry.CONTENT_URI, null, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        cursor = mContext.getContentResolver().query(DetailsEntry.buildDetailsByMovieIdUri(TEST_MOVIE_ID), null, null, null, null);
        assertEquals("ERROR: Provider could not delete all records from the Details table.", 0, cursor.getCount());
        cursor.close();

        // TEST: can we delete all records from the Reviews table via the Provider?
        TestHelper.insertTableData(mContext, PostersEntry.TABLE_NAME, TestHelper.createReviewsValues());
        mContext.getContentResolver().registerContentObserver(ReviewsEntry.CONTENT_URI, true, tco);
        mContext.getContentResolver().delete(ReviewsEntry.CONTENT_URI, null, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);
        cursor = mContext.getContentResolver().query(ReviewsEntry.buildReviewsByMovieIdUri(TEST_MOVIE_ID), null, null, null, null);
        assertEquals("ERROR: Provider could not delete all records from the Reviews table.", 0, cursor.getCount());
        cursor.close();
    }

    public void testBulkInsert() {
        int insertCount;
        Cursor cursor;

        TestHelper.TestContentObserver contentObserver = TestHelper.getTestContentObserver();
        Map<String, ContentValues[]> bulkData = createBulkContent(BULK_INSERT_RECORDS_TO_INSERT);

        // empty the database to ensure no collision of data
        deleteAllTableData();
        cursor = mContext.getContentResolver().query(PostersEntry.CONTENT_URI, null, null, null, null);
        assertEquals("ERROR - Bulk Insert Prep: Not all data was deleted from the POSTERS table!", 0, cursor.getCount());
        cursor = mContext.getContentResolver().query(DetailsEntry.CONTENT_URI, null, null, null, null);
        assertEquals("ERROR - Bulk Insert Prep: Not all data was deleted from the DETAILS table!", 0, cursor.getCount());
        cursor = mContext.getContentResolver().query(ReviewsEntry.CONTENT_URI, null, null, null, null);
        assertEquals("ERROR - Bulk Insert Prep: Not all data was deleted from the REVIEWS table!", 0, cursor.getCount());

        // TEST: can we bulk insert into the POSTER table
        mContext.getContentResolver().registerContentObserver(PostersEntry.CONTENT_URI, true, contentObserver);
        insertCount = mContext.getContentResolver().bulkInsert(PostersEntry.CONTENT_URI, bulkData.get(PostersEntry.TABLE_NAME));
        contentObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(contentObserver);
        assertEquals("ERROR: Bulk insert into the POSTERS table failed!", BULK_INSERT_RECORDS_TO_INSERT, insertCount);
        cursor = mContext.getContentResolver().query(PostersEntry.CONTENT_URI, null, null, null, null);
        assertEquals("ERROR: Query after POSTERS table bulk returned incorrect row count!", BULK_INSERT_RECORDS_TO_INSERT, cursor.getCount());
        // TEST: is the cursor empty?
        assertTrue("Empty cursor returned when querying POSTERS table", cursor.moveToFirst());
        for (ContentValues content : bulkData.get(PostersEntry.TABLE_NAME)) {
            validateQueryResults(cursor, PostersEntry.TABLE_NAME, content);
            cursor.moveToNext();
        }
        cursor.close();

        // TEST: can we bulk insert into the DETAILS table
        mContext.getContentResolver().registerContentObserver(DetailsEntry.CONTENT_URI, true, contentObserver);
        insertCount = mContext.getContentResolver().bulkInsert(DetailsEntry.CONTENT_URI, bulkData.get(DetailsEntry.TABLE_NAME));
        contentObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(contentObserver);
        assertEquals("ERROR: Bulk insert into the DETAILS table failed!", BULK_INSERT_RECORDS_TO_INSERT, insertCount);
        cursor = mContext.getContentResolver().query(DetailsEntry.CONTENT_URI, null, null, null, null);
        assertEquals("ERROR: Query after DETAILS table bulk returned incorrect row count!", BULK_INSERT_RECORDS_TO_INSERT, cursor.getCount());
        // TEST: is the cursor empty?
        assertTrue("Empty cursor returned when querying DETAILS table", cursor.moveToFirst());
        for (ContentValues content : bulkData.get(DetailsEntry.TABLE_NAME)) {
            validateQueryResults(cursor, DetailsEntry.TABLE_NAME, content);
            cursor.moveToNext();
        }
        cursor.close();

        // TEST: can we bulk insert into the REVIEWS table
        mContext.getContentResolver().registerContentObserver(ReviewsEntry.CONTENT_URI, true, contentObserver);
        insertCount = mContext.getContentResolver().bulkInsert(ReviewsEntry.CONTENT_URI, bulkData.get(ReviewsEntry.TABLE_NAME));
        contentObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(contentObserver);
        assertEquals("ERROR: Bulk insert into the REVIEWS table failed!", BULK_INSERT_RECORDS_TO_INSERT, insertCount);
        cursor = mContext.getContentResolver().query(ReviewsEntry.CONTENT_URI, null, null, null, null);
        assertEquals("ERROR: Query after REVIEWS table bulk returned incorrect row count!", BULK_INSERT_RECORDS_TO_INSERT, cursor.getCount());
        // TEST: is the cursor empty?
        assertTrue("Empty cursor returned when querying REVIEWS table", cursor.moveToFirst());
        for (ContentValues content : bulkData.get(ReviewsEntry.TABLE_NAME)) {
            validateQueryResults(cursor, ReviewsEntry.TABLE_NAME, content);
            cursor.moveToNext();
        }
        cursor.close();
    }
}




























