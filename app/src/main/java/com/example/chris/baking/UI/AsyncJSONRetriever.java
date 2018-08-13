package com.example.chris.baking.UI;

import android.os.AsyncTask;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.Utils.JsonParser;
import com.example.chris.baking.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AsyncJSONRetriever extends AsyncTask<Void,Void,String> {

    private URL url;
    private List<Recipe> recipies;

    private AsyncJSONRetrieverCompleted<List> callback;

    AsyncJSONRetriever(URL url, AsyncJSONRetrieverCompleted<List> cb){
        this.url = url;
        this.callback = cb;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String jsonResults = NetworkUtils.getJSONData(url);
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
