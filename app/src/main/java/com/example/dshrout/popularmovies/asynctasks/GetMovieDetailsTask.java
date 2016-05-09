package com.example.dshrout.popularmovies.asynctasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dshrout.popularmovies.R;
import com.example.dshrout.popularmovies.helper.MovieCard;
import com.example.dshrout.popularmovies.helper.PopularMovies;
import com.squareup.picasso.Picasso;

/**
 * Created by DShrout on 5/3/2016
 */
public class GetMovieDetailsTask extends AsyncTask<String, Void, MovieCard> {
    private MovieCard mMovieDetails;
    private View mRootView;
    private Context mContext;

    public GetMovieDetailsTask(Context context, View rootView) {
        mRootView = rootView;
        mContext = context;
    }

    private boolean NetworkAvailable() {
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    private void populateLayout() {
        TextView title = ((TextView) mRootView.findViewById(R.id.moviedetail_title));
        ImageView poster = ((ImageView) mRootView.findViewById(R.id.moviedetail_poster));
        TextView date = ((TextView) mRootView.findViewById(R.id.moviedetail_releasedate));
        TextView runtime = ((TextView) mRootView.findViewById(R.id.moviedetail_runtime));
        TextView rating = ((TextView) mRootView.findViewById(R.id.moviedetail_userrating));
        TextView summary = ((TextView) mRootView.findViewById(R.id.moviedetail_summary));

        if (mMovieDetails.getTitle().length()>0) {
            title.setText(mMovieDetails.getTitle());
        }

        if (mMovieDetails.getPosterPath().length()>0) {
            Picasso.with(mContext).load(mMovieDetails.getPosterPath()).into(poster);
            //Glide.with(getActivity()).load(mMovieDetails.getPosterPath()).into(poster);
        } else {
            poster.setImageResource(R.drawable.no_image_found);
        }

        if (mMovieDetails.getReleaseDate().length()>0) {
            date.setText(mMovieDetails.getReleaseDate().trim().substring(0, 4));
        }

        if (mMovieDetails.getRuntime().length()>0) {
            runtime.setText(mMovieDetails.getRuntime().trim() + "min");
        }

        if (mMovieDetails.getVoterAverage().length()>0) {
            rating.setText(mMovieDetails.getVoterAverage().trim() + "/10");
        }

        if (mMovieDetails.getSummary().length()>0) {
            summary.setText(mMovieDetails.getSummary());
        }
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
            PopularMovies popMovies = new PopularMovies(mContext);
            // make sure we have something to work with
            if (params.length == 0 || params[0].equals("")) {
                return null;
            }

            return popMovies.GetDetails(params[0]);
        } else {
            return new MovieCard();
        }

    }
}
