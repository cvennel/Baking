package com.example.chris.baking.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.Database.RecipeDatabase;
import com.example.chris.baking.Database.RecipeRepository;
import com.example.chris.baking.R;
import com.example.chris.baking.Utils.NetworkUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private URL mRecipeLocation;

    public RecipeCardAdapter mAdapter;

    RecipeRepository mRecipeRepository;
    SharedPreferences mPreferences;
    RecipeDatabase mRecipeDatabase;

    BroadcastReceiver mNetworkStateReceiver;
    Boolean mNetworkStateReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeRepository = new RecipeRepository(getApplication());

        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecipeCardAdapter(this, this);
        recyclerView.setAdapter(mAdapter);


        mNetworkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (NetworkUtils.isOnline(context)) {
                    downloadRecipes();
                }
            }

        };


        try {
            mRecipeLocation = new URL(getString(R.string.json_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mRecipeDatabase = RecipeDatabase.getDatabase(getApplicationContext());

        mPreferences = getPreferences(Context.MODE_PRIVATE);

        String lastUpdatePrefString = mPreferences.getString(getString(R.string.date_last_updated_key), "default");

        if (lastUpdatePrefString.equals("default")) {
            downloadRecipes();

        } else {
            LocalDate lastUpdate = LocalDate.parse(lastUpdatePrefString);

            if (ChronoUnit.DAYS.between(lastUpdate, LocalDate.now()) == 0 && mRecipeRepository.getRecipes() != null && 1 == 2) {
                mAdapter.setRecipeList(mRecipeRepository.getRecipes());
            } else {

                downloadRecipes();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter.getRecipeList() == null) {
            downloadRecipes();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNetworkStateReceiverRegistered) {
            unregisterReceiver(mNetworkStateReceiver);
            mNetworkStateReceiverRegistered = false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void downloadRecipes() {

        if (!NetworkUtils.isOnline(this)) {

            registerReceiver(mNetworkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            mNetworkStateReceiverRegistered = true;

            Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_LONG).show();
            if (mRecipeRepository != null) {
                mAdapter.setRecipeList(mRecipeRepository.getRecipes());
            }
            return;
        }


        if (mRecipeLocation != null) {

            AsyncJSONRetrieverCompleted listener = new AsyncJSONRetrieverCompleted() {
                @Override
                public void onTaskComplete(Object result) {

                    mAdapter.setRecipeList((List<Recipe>) result);
                    mAdapter.notifyDataSetChanged();


                    mRecipeRepository.updateRecipes(mAdapter.getRecipeList());

                    mPreferences.edit().putString(getString(R.string.date_last_updated_key), LocalDate.now().toString()).apply();
                }
            };

            BufferedReader reader = null;
            StringBuilder builder = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(getAssets().open("JSON.txt")));

                String mline;
                while ((mline = reader.readLine()) != null) {
                    builder.append(mline);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            //To test changes to JSON change testJsonResults to builder instead of null
            new AsyncJSONRetriever(mRecipeLocation, listener, null /*builder*/).execute();
        }
    }

}
