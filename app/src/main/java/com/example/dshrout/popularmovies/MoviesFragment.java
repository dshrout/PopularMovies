package com.example.dshrout.popularmovies;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.dshrout.popularmovies.movies.Movie;
import com.example.dshrout.popularmovies.movies.PopularMovies;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    private ImageAdapter mMovieCardsAdapter;
    private final String TMDB_API_KEY = "bfaf98a2c85c264e97326dbadfe63a1e";

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieCards();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Create a list to hold the movie cards and load it
        mMovieCardsAdapter = new ImageAdapter(getActivity());
        updateMovieCards();

        // attach adapter to view
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        // if I sleep here for even just a half a second, it works.
        gridView.setAdapter(mMovieCardsAdapter);
        return rootView;
    }

    private void updateMovieCards(){
        new GetMovieCardsTask().execute();
    }

    public class GetMovieCardsTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        @Override
        protected void onPostExecute(ArrayList<Movie> imageUrls) {
            super.onPostExecute(imageUrls);
            mMovieCardsAdapter.clear();
            mMovieCardsAdapter.addAll(imageUrls);
            mMovieCardsAdapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            PopularMovies popMovies = new PopularMovies();
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = sharedPrefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
            String sortOrder = sharedPrefs.getString(getString(R.string.pref_sortorder_key), getString(R.string.pref_sortorder_default));
            return popMovies.GetMovies(sortBy, sortOrder, TMDB_API_KEY);
        }
    }
}
