package com.example.dshrout.popularmovies;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    private ImageAdapter mMovieCardsAdapter;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Create a list to hold the movie cards and load it
        mMovieCardsAdapter = new ImageAdapter(getActivity());
        updateMovieCards();

        // attach adapter to view
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMovieCardsAdapter);
        return rootView;
    }

    private void updateMovieCards(){
        new GetMovieCardsTask().execute();
    }

    public class GetMovieCardsTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected void onPostExecute(String[] imageUrls) {
            super.onPostExecute(imageUrls);
            mMovieCardsAdapter.clear();
            mMovieCardsAdapter.addAll(imageUrls);
        }

        @Override
        protected String[] doInBackground(Void... params) {
            return new String[] {
                    "http://i.imgur.com/lfqsFELb.jpg",
                    "http://i.imgur.com/ipzUCGub.jpg",
                    "http://i.imgur.com/KGl97Wsb.jpg",
                    "http://i.imgur.com/R4cy166b.jpg",
                    "http://i.imgur.com/U4LFtL6b.jpg",
                    "http://i.imgur.com/ilmNYmKb.jpg"
            };
        }
    }
}
