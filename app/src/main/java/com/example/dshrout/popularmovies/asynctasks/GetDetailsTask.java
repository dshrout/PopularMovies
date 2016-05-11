package com.example.dshrout.popularmovies.asynctasks;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.dshrout.popularmovies.data.PopMoviesContract;
import com.example.dshrout.popularmovies.helper.PopulateDatabase;

/**
 * Created by DShrout on 5/3/2016
 */
public class GetDetailsTask extends AsyncTask<Long, Void, Void> {
    private Context mContext;

    public GetDetailsTask(Context context) {
        mContext = context;
    }

    private boolean DetailsAvailable(long movieId) {
        Cursor cursor = mContext.getContentResolver().query(PopMoviesContract.DetailsEntry.buildDetailsByMovieIdUri((int)movieId), null, PopMoviesContract.DetailsEntry.COLUMN_MOVIE_ID + " = " + movieId, null, null);
        boolean result = false;
        if (cursor != null && cursor.moveToFirst()) {
            result = true;
            cursor.close();
        }
        return result;
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

    @Override
    protected Void doInBackground(Long... params) {
        // if no movie id was passed in then exit
        if (params.length == 0 || params[0] == 0) {
            return null;
        }

        // if the details are already in the db then exit
        if (DetailsAvailable(params[0])) {
            return null;
        }

        // if we get here then we do not yet have the movie details
        // if the network is available, get the details from TMDB
        if(NetworkAvailable()) {
            PopulateDatabase popMovies = new PopulateDatabase(mContext);
            popMovies.GetDetails(Long.toString(params[0]));
            return null;
        } else {
            return null;
        }
    }
}
