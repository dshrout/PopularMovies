package com.example.dshrout.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dshrout.popularmovies.data.PopMoviesContract;


public class MainActivity extends ActionBarActivity implements MainFragment.Callback {
    private final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_details_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_details_container, new MovieDetailsFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onItemSelected(Uri detailsUri) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailsFragment.MOVIE_DETAIL_URI, detailsUri);

            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent detailsIntent = new Intent(this, MovieDetailsActivity.class).setData(detailsUri);
            startActivity(detailsIntent);
        }
    }
}
