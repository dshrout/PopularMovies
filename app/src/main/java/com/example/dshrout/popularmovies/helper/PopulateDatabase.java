package com.example.dshrout.popularmovies.helper;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dshrout.popularmovies.BuildConfig;
import com.example.dshrout.popularmovies.data.DetailsItem;
import com.example.dshrout.popularmovies.data.PopMoviesContract;
import com.example.dshrout.popularmovies.data.PostersItem;
import com.example.dshrout.popularmovies.data.PostersList;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PopulateDatabase {
    private final String TMDB_BASE_URL = "http://api.themoviedb.org/3/";
    private final Context mContext;

    public PopulateDatabase(Context context) {
        mContext = context;
    }

    // get a list of movie posters
    public void GetPosters(String sortBy) {
        String urlString = TMDB_BASE_URL + "discover/movie?" +
                "sort_by=" + sortBy + ".desc" +
                "&api_key=" + BuildConfig.TMDB_API_KEY +
                "&vote_count.gte=5";
        String jsonData = getJsonData(urlString);
        if (jsonData != null && jsonData.length() > 0) {
            parsePostersJSON(jsonData);
        }
    }

    // get a movie's details
    public void GetDetails(String movieId) {
        String urlString = TMDB_BASE_URL + "movie/" + movieId + "?api_key=" + BuildConfig.TMDB_API_KEY;
        String jsonData = getJsonData(urlString);
        if (jsonData != null && jsonData.length() > 0) {
            parseDetailsJSON(jsonData);
        }
    }

    private String getJsonData(String urlString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            StringBuilder buffer = new StringBuilder();
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            String line;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            return buffer.toString();

        } catch (IOException e) {
            Log.e("getJsonData", "Error ", e);
            return "";
        } catch (Exception e) {
            Log.e("getJsonData", "Error ", e);
            return "";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("getJsonData", "Error closing stream", e);
                }
            }
        }
    }

    @Nullable
    private void parsePostersJSON(String postersData) {
        try {
            ArrayList<ContentValues> postersList = new ArrayList<>();
            List<PostersItem> posters = new Gson().fromJson(postersData, PostersList.class).results;

            for (PostersItem poster : posters) {
                ContentValues posterValues = new ContentValues();
                posterValues.put(PopMoviesContract.PostersEntry.COLUMN_MOVIE_ID, poster.id);
                if (poster.poster_path.length() > 0 && !poster.poster_path.equals("null")) {
                    posterValues.put(PopMoviesContract.PostersEntry.COLUMN_POSTER_PATH, poster.poster_path);
                } else {
                    posterValues.put(PopMoviesContract.PostersEntry.COLUMN_POSTER_PATH, "");
                }
                postersList.add(posterValues);
            }

            if (postersList.size() > 0) {
                mContext.getContentResolver().bulkInsert(PopMoviesContract.PostersEntry.CONTENT_URI, postersList.toArray(new ContentValues[postersList.size()]));
            }
        } catch (Exception e) {
            Log.e("parsePostersJSON", "Exception", e);
        }
    }

    @Nullable
    private void parseDetailsJSON(String detailsData) {
        try {
            ContentValues[] detailsArray = new ContentValues[1];
            DetailsItem details = new Gson().fromJson(detailsData, DetailsItem.class);
            ContentValues detailValues = new ContentValues();

            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_MOVIE_ID, details.id);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_TITLE, details.title);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_TAGLINE, details.tagline);
            if (details.poster_path.length() > 0 && !details.poster_path.equals("null")) {
                detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_POSTER_PATH, details.poster_path);
            } else {
                detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_POSTER_PATH, "");
            }
            if (details.backdrop_path.length() > 0 && !details.backdrop_path.equals("null")) {
                detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_BACKDROP_PATH, details.backdrop_path);
            } else {
                detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_BACKDROP_PATH, "");
            }
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_OVERVIEW, details.overview);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_RELEASE_DATE, details.release_date);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_RUNTIME, details.runtime);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_POPULARITY, details.popularity);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_VOTE_AVERAGE, details.vote_average);
            detailValues.put(PopMoviesContract.DetailsEntry.COLUMN_VOTE_COUNT, details.vote_count);

            detailsArray[0] = detailValues;
            mContext.getContentResolver().bulkInsert(PopMoviesContract.DetailsEntry.CONTENT_URI, detailsArray);
        } catch (Exception e) {
            Log.e("parsePostersJSON", "Exception", e);
        }
    }
}
