package com.example.chris.baking;

import java.io.IOException;
import java.net.URL;

public class JsonRetriever {

    public static String getJson(URL url){
        try {
            String jsonResults = NetworkUtils.getJSONData(url);
            return jsonResults;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
