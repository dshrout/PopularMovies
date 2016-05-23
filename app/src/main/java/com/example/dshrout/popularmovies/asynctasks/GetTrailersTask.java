package com.example.dshrout.popularmovies.asynctasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.dshrout.popularmovies.MovieDetailsFragment;
import com.example.dshrout.popularmovies.data.TrailersItem;
import com.example.dshrout.popularmovies.helper.FetchData;

import java.util.ArrayList;

/**
 * Created by DShrout on 5/23/2016.
 */
public class GetTrailersTask extends AsyncTask<Long, Void, ArrayList<TrailersItem>> {
    private Context mContext;
    private MovieDetailsFragment mFragment;

    public GetTrailersTask(Context context, MovieDetailsFragment fragment) {
        mContext = context;
        mFragment = fragment;
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
    protected ArrayList<TrailersItem> doInBackground(Long... params) {
        // if no movie id was passed in or details are already available then exit
        if (params.length == 0 || params[0] == 0) {
            return new ArrayList<>();
        }

        // if we get here then we do not yet have the movie details
        // if the network is available, get the details from TMDB
        if(NetworkAvailable()) {
            FetchData fetchData = new FetchData(mContext);
            return fetchData.GetTrailers(Long.toString(params[0]));
        }
        return new ArrayList<>();
    }

    protected void onPostExecute(ArrayList<TrailersItem> result) {
        if (result != null) {
            mFragment.populateTrailersList(result);
        }
    }
}
