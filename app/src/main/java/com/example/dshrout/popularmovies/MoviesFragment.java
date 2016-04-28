package com.example.dshrout.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.dshrout.popularmovies.asynctasks.GetMovieCardsTask;
import com.example.dshrout.popularmovies.helper.MovieCard;


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
        new GetMovieCardsTask(getActivity(), rootView, mMovieCardsAdapter).execute();
    }

}
