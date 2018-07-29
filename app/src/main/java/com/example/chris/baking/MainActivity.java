package com.example.chris.baking;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private URL mRecipieLocation;

    private AsyncJSONRetrieverCompleted mCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            mRecipieLocation = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (mRecipieLocation != null){

            AsyncJSONRetrieverCompleted listener = new AsyncJSONRetrieverCompleted() {
                @Override
                public void onTaskComplete(Object result) {
                    List<Recipe> recipes = (List<Recipe>) result;
                    addFragment(recipes);
                }
            };

            new AsyncJSONRetriever(mRecipieLocation, listener).execute();
        }


    }


    protected void addFragment(List<Recipe> recipies){

        FragmentManager fragmentManager = getSupportFragmentManager();



        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RecipeCardFragment fragment;
        for(int i = 0; i < recipies.size(); i++){

            fragment = new RecipeCardFragment();

            Recipe recipe = recipies.get(i);

            String title = recipe.getName();
            fragment.setTitle(title);

            String imageLocation = recipe.getImage();
            fragment.setImage(imageLocation);

            String fragmentID = "id_" + i;
            fragmentTransaction.add(R.id.recipie_card_container, fragment, fragmentID);
        }

        fragmentTransaction.commit();
    }


}
