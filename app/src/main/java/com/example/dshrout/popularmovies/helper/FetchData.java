package com.example.dshrout.popularmovies.helper;

import android.content.Context;
import android.util.Log;

import com.example.dshrout.popularmovies.BuildConfig;
import com.example.dshrout.popularmovies.data.ReviewsItem;
import com.example.dshrout.popularmovies.data.ReviewsList;
import com.example.dshrout.popularmovies.data.TrailersItem;
import com.example.dshrout.popularmovies.data.TrailersList;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DShrout on 5/19/2016.
 */
public class FetchData {
    private final String TMDB_BASE_URL = "http://api.themoviedb.org/3/";
    private Context mContext;
    private PopulateDatabase mPopDb;

    public FetchData (Context context) {
        mContext = context;
        mPopDb = new PopulateDatabase(context);
    }

    public void GetPosters(String sortBy) {
        String urlString = TMDB_BASE_URL + "discover/movie?" +
                "sort_by=" + sortBy + ".desc" +
                "&api_key=" + BuildConfig.TMDB_API_KEY +
                "&vote_count.gte=5";
        String jsonData = getJsonData(urlString);
        if (jsonData != null && jsonData.length() > 0) {
            mPopDb.populatePostersTable(jsonData);
        }
    }

    // get a movie's details
    public void GetDetails(String movieId) {
        String urlString = TMDB_BASE_URL + "movie/" + movieId + "?api_key=" + BuildConfig.TMDB_API_KEY;
        String jsonData = getJsonData(urlString);
        if (jsonData != null && jsonData.length() > 0) {
            mPopDb.populateDetailsTable(jsonData);
        }
    }

    public ArrayList<ReviewsItem> GetReviews(String movieId) {
        String urlString = TMDB_BASE_URL + "movie/" + movieId + "/reviews?api_key=" + BuildConfig.TMDB_API_KEY;
        String jsonData = getJsonData(urlString);
        if (jsonData != null && jsonData.length() > 0) {
            ArrayList<ReviewsItem> reviewsItems = null;
            try {
                reviewsItems = (ArrayList<ReviewsItem>) new Gson().fromJson(jsonData, ReviewsList.class).results;
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            return reviewsItems;
        }

        return new ArrayList<>();
    }

    public ArrayList<TrailersItem> GetTrailers(String movieId) {
        String urlString = TMDB_BASE_URL + "movie/" + movieId + "/videos?api_key=" + BuildConfig.TMDB_API_KEY;
        String jsonData = getJsonData(urlString);
        if (jsonData != null && jsonData.length() > 0) {
            ArrayList<TrailersItem> trailersItems = null;
            try {
                trailersItems = (ArrayList<TrailersItem>) new Gson().fromJson(jsonData, TrailersList.class).results;
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            return trailersItems;
        }

        return new ArrayList<>();
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
}
