package com.example.dshrout.popularmovies.asynctasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.example.dshrout.popularmovies.data.ReviewsItem;
import com.example.dshrout.popularmovies.helper.FetchData;

import java.util.ArrayList;

/**
 * Created by DShrout on 5/20/2016.
 */
public class GetReviewsTask extends AsyncTask<Long, Void, Void> {
    private Context mContext;
    private ArrayAdapter<ReviewsItem> mReviewsAdapter;

    public GetReviewsTask(Context context, ArrayAdapter<ReviewsItem> reviewsAdapter) {
        mContext = context;
        mReviewsAdapter = reviewsAdapter;
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
        // if no movie id was passed in or details are already available then exit
        if (params.length == 0 || params[0] == 0) {
            return null;
        }

        // if we get here then we do not yet have the movie details
        // if the network is available, get the details from TMDB
        if(NetworkAvailable()) {
            FetchData fetchData = new FetchData(mContext);
            fetchData.GetReviews(Long.toString(params[0]));
        }
        return null;
    }

    protected void onPostExecute(ArrayList<ReviewsItem> result) {
        if (result != null) {
            mReviewsAdapter.clear();
            for(ReviewsItem review : result) {
                mReviewsAdapter.add(review);
            }
        }
    }
}
