package com.example.dshrout.popularmovies.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.dshrout.popularmovies.data.PopMoviesContract;
import com.example.dshrout.popularmovies.data.PopMoviesDbHelper;

/**
 * Created by DShrout on 2/25/2016.
 */
public class TestHelper {
    static final int TEST_MOVIE_ID = 550;
    static final String TEST_REVIEW_ID = java.util.UUID.randomUUID().toString();

    public static ContentValues createPosterValues() {
        ContentValues posterValues = new ContentValues();
        posterValues.put(PopMoviesContract.PostersEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
        posterValues.put(PopMoviesContract.PostersEntry.COLUMN_POSTER_PATH, "/2lECpi35Hnbpa4y46JX0aY3AWTy.jpg");
        return posterValues;
    }

    public static ContentValues createDetailsValues() {
        ContentValues detailsValues = new ContentValues();
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_BACKDROP_PATH, "/hNFMawyNDWZKKHU4GYCBz1krsRM.jpg");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_OVERVIEW, "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy.");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_RELEASE_DATE, "1999-10-14");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_TITLE, "Fight Club");
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_VOTE_COUNT, 3185);
        detailsValues.put(PopMoviesContract.DetailsEntry.COLUMN_RUNTIME, 139);
        return detailsValues;
    }

    public static ContentValues createReviewsValues() {
        ContentValues reviewsValues = new ContentValues();
        reviewsValues.put(PopMoviesContract.ReviewsEntry.COLUMN_REVIEW_ID, TEST_REVIEW_ID);
        reviewsValues.put(PopMoviesContract.ReviewsEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
        reviewsValues.put(PopMoviesContract.ReviewsEntry.COLUMN_AUTHOR, "mfreeman");
        reviewsValues.put(PopMoviesContract.ReviewsEntry.COLUMN_CONTENT, "I thought this film rocked!");
        return reviewsValues;
    }

    public static boolean insertTableData(Context context, String tableName, ContentValues content) {
        SQLiteDatabase db = new PopMoviesDbHelper(context).getWritableDatabase();
        return db.insert(tableName, null, content) != -1;
    }

    public static void deleteTableData(Context context, String tableName) {
        PopMoviesDbHelper dbHelper = new PopMoviesDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(tableName, null, null);
        db.close();
    }

    public static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    public static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}





































