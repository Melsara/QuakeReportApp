package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>{

    private String mUrl;
    public static final String LOG_TAG = EarthquakeLoader.class.getName();

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;

    }

    @Override
    protected void onStartLoading() {
        Log.i("startLoading()", "startLoading() was called");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i("loadInBackground()", "loadInBackground() was called");
        if (mUrl == null) {
            return null;
        }

        final List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
