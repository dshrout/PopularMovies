package com.example.dshrout.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dshrout.popularmovies.helper.MovieCard;
import com.example.dshrout.popularmovies.helper.PopularMovies;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    private ImageAdapter mMovieCardsAdapter;

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
        if (mMovieCardsAdapter != null)
            updateMovieCards(this.getActivity().findViewById(R.id.fragment));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Create a list to hold the movie cards then update it
        mMovieCardsAdapter = new ImageAdapter(getActivity());
        updateMovieCards(rootView);

        // attach adapter to view
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMovieCardsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieCard movieCard = (MovieCard) mMovieCardsAdapter.getItem(position);
                if (movieCard != null) {
                    Intent displayMovieDetails = new Intent(MoviesFragment.this.getActivity(), MovieDetailsActivity.class);
                    displayMovieDetails.putExtra(Intent.EXTRA_TEXT, movieCard.getId());
                    MoviesFragment.this.startActivity(displayMovieDetails);
                }
            }
        });

        return rootView;
    }

    private void updateMovieCards(View rootView){
        new GetMovieCardsTask(rootView).execute();
    }

    public class GetMovieCardsTask extends AsyncTask<Void, Void, ArrayList<MovieCard>> {
        private View mRootView;

        public GetMovieCardsTask(View rootView) {
            mRootView = rootView;
        }

        private void setViews() {
            TextView textView = (TextView) mRootView.findViewById(R.id.empty);
            GridView gridView = (GridView) mRootView.findViewById(R.id.gridview_movies);
            if (mMovieCardsAdapter.getCount() > 1) {
                gridView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            } else {
                gridView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
        }

        private boolean NetworkAvailable() {
            ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connManager.getActiveNetworkInfo();
            if (info == null) return false;
            NetworkInfo.State network = info.getState();
            return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieCard> movies) {
            super.onPostExecute(movies);
            mMovieCardsAdapter.clear();
            mMovieCardsAdapter.addAll(movies);
            mMovieCardsAdapter.notifyDataSetChanged();
            setViews();
        }

        @Override
        protected ArrayList<MovieCard> doInBackground(Void... params) {
            // TODO: Get data from database. If db is empty, and network is available, query tmdb and populate database
            if (NetworkAvailable()) {
                PopularMovies popMovies = new PopularMovies();
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortBy = sharedPrefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
                String sortOrder = sharedPrefs.getString(getString(R.string.pref_sortorder_key), getString(R.string.pref_sortorder_default));
                return popMovies.GetMovies(sortBy, sortOrder);
            } else {
                return new ArrayList<>();
            }
        }
    }
}
