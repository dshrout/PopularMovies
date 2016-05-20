package com.example.dshrout.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailsFragment.MOVIE_DETAIL_URI, getIntent().getData());

            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_details_container, fragment)
                    .commit();
        }
    }
}
