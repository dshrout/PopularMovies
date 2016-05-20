package com.example.dshrout.popularmovies.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.example.dshrout.popularmovies.R;
import com.example.dshrout.popularmovies.helper.FetchData;

/**
 * Created by DShrout on 4/13/2016.
 */
public class GetPostersTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    public GetPostersTask(Context context) {
        mContext = context;
    }

    private boolean NetworkAvailable() {
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    @Override
    protected Void doInBackground(Void... params) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortBy = sharedPrefs.getString(mContext.getString(R.string.pref_sortby_key), mContext.getString(R.string.pref_sortby_default));

        if (NetworkAvailable()) {
            FetchData fetchData = new FetchData(mContext);
            fetchData.GetPosters(sortBy);
            return null;
        } else {
            return null;
        }
    }
}
