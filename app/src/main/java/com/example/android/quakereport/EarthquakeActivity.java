/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>  {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    static final String URL_KEY = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=20";
    private EarthquakeAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    TextView emptyView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        emptyView = (TextView) findViewById(R.id.emptyView);
        earthquakeListView.setEmptyView(emptyView);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake currentEarthquake = mAdapter.getItem(position);
                String url = currentEarthquake.getUrl();
                Intent openEartquake = new Intent(Intent.ACTION_VIEW);
                openEartquake.setData(Uri.parse(url));
                startActivity(openEartquake);
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            Log.i("initLoader()", "initLoader() is about to be called");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        } else {
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);

        }

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.i("onCreateLoader()", "onCreateLoader() was called");
        return new EarthquakeLoader(this, URL_KEY);

    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i("onLoadFinished()", "onLoadFinished() was called");
        progressBar.setVisibility(View.GONE);
        mAdapter.clear();
        if (earthquakes != null && !earthquakes.isEmpty()){
            mAdapter.addAll(earthquakes);
        } else {
            emptyView.setText(R.string.empty_view);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i("onLoadReset()", "onLoadReset() was called");
        mAdapter.clear();

    }

}
