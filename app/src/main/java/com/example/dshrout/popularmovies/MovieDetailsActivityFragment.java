package com.example.dshrout.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dshrout.popularmovies.helper.MovieCard;
import com.example.dshrout.popularmovies.helper.PopularMovies;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {
    private MovieCard mMovieDetails;
    private View mRootView;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        String movieId;

        // get the intent used to launch this activity
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            // get the movie id from the intent
            movieId = intent.getExtras().getString(Intent.EXTRA_TEXT);
            // new up and populate our movie details
            mMovieDetails = new MovieCard();
            getMovieDetails(movieId);
        }

        return mRootView;
    }

    private void populateLayout() {
        TextView title = ((TextView) mRootView.findViewById(R.id.moviedetail_title));
        ImageView poster = ((ImageView) mRootView.findViewById(R.id.moviedetail_poster));
        TextView date = ((TextView) mRootView.findViewById(R.id.moviedetail_releasedate));
        TextView runtime = ((TextView) mRootView.findViewById(R.id.moviedetail_runtime));
        TextView rating = ((TextView) mRootView.findViewById(R.id.moviedetail_userrating));
        TextView summary = ((TextView) mRootView.findViewById(R.id.moviedetail_summary));

        if (mMovieDetails.getTitle().length()>0)
            title.setText(mMovieDetails.getTitle());
        if (mMovieDetails.getPosterPath().length()>0)
            Picasso.with(getActivity()).load(mMovieDetails.getPosterPath()).into(poster);
        else
            poster.setImageResource(R.drawable.no_image_found);
        if (mMovieDetails.getReleaseDate().length()>0)
            date.setText(mMovieDetails.getReleaseDate().trim().substring(0, 4));
        if (mMovieDetails.getRuntime().length()>0)
            runtime.setText(mMovieDetails.getRuntime().trim() + "min");
        if (mMovieDetails.getVoterAverage().length()>0)
            rating.setText(mMovieDetails.getVoterAverage().trim() + "/10");
        if (mMovieDetails.getSummary().length()>0)
            summary.setText(mMovieDetails.getSummary());
    }

    private void getMovieDetails(String movieId){
        new GetMovieDetailsTask().execute(movieId);
    }

    public class GetMovieDetailsTask extends AsyncTask<String, Void, MovieCard> {
        private boolean NetworkAvailable() {
            ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connManager.getActiveNetworkInfo();
            if (info == null) return false;
            NetworkInfo.State network = info.getState();
            return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
        }

        @Override
        protected void onPostExecute(MovieCard movieCard) {
            super.onPostExecute(movieCard);
            mMovieDetails = movieCard;
            populateLayout();
        }

        @Override
        protected MovieCard doInBackground(String... params) {
            if(NetworkAvailable()) {
                PopularMovies popMovies = new PopularMovies();
                // make sure we have something to work with
                if (params.length == 0 || params[0] == "")
                    return null;

                return popMovies.GetMovie(params[0]);
            } else {
                return new MovieCard();
            }

        }
    }
}
