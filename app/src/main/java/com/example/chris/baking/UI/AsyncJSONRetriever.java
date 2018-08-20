package com.example.chris.baking.UI;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.Database.RecipeDatabase;
import com.example.chris.baking.Utils.JsonParser;
import com.example.chris.baking.Utils.NetworkUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.List;

public class AsyncJSONRetriever extends AsyncTask<Void,Void,String> {

    private URL url;
    private List<Recipe> recipies;

    private String jsonResultsForTesting;
    private AsyncJSONRetrieverCompleted<List> callback;

    AsyncJSONRetriever(URL url, AsyncJSONRetrieverCompleted<List> cb, @Nullable String testJsonResults){
        this.url = url;
        this.callback = cb;
        this.jsonResultsForTesting = testJsonResults;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {

            String jsonResults;
            if(jsonResultsForTesting == null){
                jsonResults = NetworkUtils.getJSONData(url);
            } else{
                jsonResults = jsonResultsForTesting;
            }


            recipies = JsonParser.parseRecipies(jsonResults);





            return jsonResults;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        callback.onTaskComplete(recipies);
    }
}

interface AsyncJSONRetrieverCompleted<T>{

    void onTaskComplete(T result);
}
