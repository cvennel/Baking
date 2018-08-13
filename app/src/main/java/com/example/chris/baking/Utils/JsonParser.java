package com.example.chris.baking.Utils;

import com.example.chris.baking.DataTypes.Recipe;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public static List<Recipe> parseRecipies(String json){

        try {
            JSONArray resultsArray = new JSONArray(json);

            GsonBuilder builder = new GsonBuilder();

            List<Recipe> recipeList = new ArrayList<>();

            for (int i =0 ; i < resultsArray.length() ; i++){
               recipeList.add(builder.create().fromJson(resultsArray.getString(i),Recipe.class));
            }

            return recipeList;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
