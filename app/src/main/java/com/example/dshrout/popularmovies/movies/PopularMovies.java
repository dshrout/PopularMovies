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
    private final String TMDB_API_KEY = "this is not the key you're looking for"; // TODO: remove this key before publishing to github
    private final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w342/";
    private final String TMDB_BASE_URL = "http://api.themoviedb.org/3/";

    public PopularMovies () {
    }

    public MovieCard GetMovie(String movieId) {
        String urlString = TMDB_BASE_URL + "movie/" + movieId + "?api_key=" + TMDB_API_KEY;
        return GetMovieData(urlString).get(0);
    }

    public ArrayList<MovieCard> GetMovies(String sortBy, String sortOrder) {
        String urlString = TMDB_BASE_URL + "discover/movie?" +
                "sort_by=" + sortBy + "." + sortOrder +
                "&api_key=" + TMDB_API_KEY;

        return GetMovieData(urlString);
    }

    public ArrayList<MovieCard> GetMovieData(String urlString) {

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
            return parseMovieData(moviesJsonStr);

        } catch (IOException e) {
            Log.e("GetMovieData", "Error ", e);
            return null;
        } catch (Exception e) {
            Log.e("GetMovieData", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("GetMovieData", "Error closing stream", e);
                }
            }
        }
    }

    @Nullable
    private ArrayList<MovieCard> parseMovieData(String movieData) {
        try {
            MovieCard movie;
            ArrayList<MovieCard> movies = new ArrayList<>();
            JSONObject jsonMovieData = new JSONObject(movieData);
            if (jsonMovieData.has("results")) {
                JSONArray jsonMovies = jsonMovieData.optJSONArray("results");
                for(int i=0; i < jsonMovies.length(); i++){
                    JSONObject jsonObject = jsonMovies.getJSONObject(i);
                    movie = new MovieCard();
                    movie.setId(jsonObject.optString("id"));
                    movie.setTitle(jsonObject.optString("title"));
                    movie.setReleaseDate(jsonObject.optString("release_date"));
                    movie.setRuntime(jsonObject.optString("runtime"));
                    movie.setVoterAverage(jsonObject.optString("vote_average"));
                    if (jsonObject.optString("poster_path").length()>0 && !jsonObject.optString("poster_path").equals("null"))
                        movie.setPosterPath(TMDB_IMAGE_PATH + jsonObject.optString("poster_path"));
                    if (jsonObject.optString("backdrop_path").length()>0 && !jsonObject.optString("backdrop_path").equals("null"))
                        movie.setBackdropPath(TMDB_IMAGE_PATH + jsonObject.optString("backdrop_path"));
                    movie.setSummary(jsonObject.optString("overview"));
                    movies.add(movie);
                }
            } else {
                movie = new MovieCard();
                movie.setId(jsonMovieData.optString("id"));
                movie.setTitle(jsonMovieData.optString("title"));
                movie.setReleaseDate(jsonMovieData.optString("release_date"));
                movie.setRuntime(jsonMovieData.optString("runtime"));
                movie.setVoterAverage(jsonMovieData.optString("vote_average"));
                if (jsonMovieData.optString("poster_path").length()>0 && !jsonMovieData.optString("poster_path").equals("null"))
                    movie.setPosterPath(TMDB_IMAGE_PATH + jsonMovieData.optString("poster_path"));
                if (jsonMovieData.optString("backdrop_path").length()>0 && !jsonMovieData.optString("backdrop_path").equals("null"))
                    movie.setBackdropPath(TMDB_IMAGE_PATH + jsonMovieData.optString("backdrop_path"));
                movie.setSummary(jsonMovieData.optString("overview"));
                movies.add(movie);
            }

            return movies;

        } catch (JSONException e) {
            Log.e("parseMovieData", "JSON Exception", e);
            return null;
        }
    }
}
