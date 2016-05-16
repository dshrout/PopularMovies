package com.example.dshrout.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MovieDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_details_container, new MovieDetailsFragment())
                    .commit();
        }
    }
}
