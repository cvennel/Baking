package com.example.chris.baking;

import android.os.AsyncTask;

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

            callback.onTaskComplete(recipies);
            return jsonResults;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

interface AsyncJSONRetrieverCompleted<ArrayList>{

    void onTaskComplete(ArrayList result);
}
