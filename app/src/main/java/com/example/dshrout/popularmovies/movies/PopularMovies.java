package com.example.dshrout.popularmovies.movies;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PopularMovies {
    public PopularMovies () {
    }

    public ArrayList<Movie> GetMovies(String sortBy, String sortOrder, String apikey) {
        String urlString;

        if(!sortBy.toLowerCase().equals("user rating"))
            sortBy = "popularity";

        if(!sortOrder.toLowerCase().equals("asc"))
            sortOrder = "desc";

        urlString = "http://api.themoviedb.org/3/discover/movie?" +
                    "sort_by=" + sortBy + "." + sortOrder +
                    "&api_key=" + apikey;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr;
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

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            moviesJsonStr = buffer.toString();
            return parseMoviesData(moviesJsonStr);

        } catch (IOException e) {
            Log.e("GetMovies", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("GetMovies", "Error closing stream", e);
                }
            }
        }
    }

    @Nullable
    private ArrayList<Movie> parseMoviesData(String moviesData) {
        try {
            JSONObject jsonMoviesData = new JSONObject(moviesData);
            JSONArray jsonMovies = jsonMoviesData.optJSONArray("results");
            ArrayList<Movie> movies = new ArrayList<>();
            Movie movie;

            for(int i=0; i < jsonMovies.length(); i++){
                JSONObject jsonObject = jsonMovies.getJSONObject(i);
                movie = new Movie();
                movie.setId(jsonObject.optString("id"));
                movie.setPosterPath(jsonObject.optString("poster_path"));
                movies.add(movie);
                if(i==19) {
                    break;
                }
            }

            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
