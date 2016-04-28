package com.example.dshrout.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dshrout.popularmovies.data.PopMoviesContract.PostersEntry;
import com.example.dshrout.popularmovies.data.PopMoviesContract.DetailsEntry;
import com.example.dshrout.popularmovies.data.PopMoviesContract.ReviewsEntry;

/*
 * Created by DShrout on 10/13/2015.
 */
public class PopMoviesDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "popularmovies.db";

    public PopMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POSTERS_TABLE = "CREATE TABLE " + PostersEntry.TABLE_NAME + " (" +
                PostersEntry._ID + " INTEGER PRIMARY KEY, " +
                PostersEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                PostersEntry.COLUMN_POSTER_PATH + " TEXT, " +
                PostersEntry.COLUMN_POPULARITY + " TEXT, " +
                PostersEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                PostersEntry.COLUMN_FAVORITE + " INTEGER);";

        final String SQL_CREATE_DETAILS_TABLE = "CREATE TABLE " + DetailsEntry.TABLE_NAME + " (" +
                DetailsEntry._ID + " INTEGER PRIMARY KEY, " +
                DetailsEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                DetailsEntry.COLUMN_ADULT + " INTEGER, " +
                DetailsEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                DetailsEntry.COLUMN_GENRE_IDS + " TEXT, " +
                DetailsEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                DetailsEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                DetailsEntry.COLUMN_OVERVIEW + " TEXT, " +
                DetailsEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                DetailsEntry.COLUMN_TITLE + " TEXT, " +
                DetailsEntry.COLUMN_VIDEO + " INTEGER, " +
                DetailsEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                DetailsEntry.COLUMN_RUNTIME + " INTEGER);";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +
                ReviewsEntry._ID + " INTEGER PRIMARY KEY, " +
                ReviewsEntry.COLUMN_REVIEW_ID + " TEXT UNIQUE NOT NULL, " +
                ReviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                ReviewsEntry.COLUMN_AUTHOR + " TEXT, " +
                ReviewsEntry.COLUMN_CONTENT + " TEXT);";

        // now actually create the tables
        sqLiteDatabase.execSQL(SQL_CREATE_POSTERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DETAILS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PostersEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DetailsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
