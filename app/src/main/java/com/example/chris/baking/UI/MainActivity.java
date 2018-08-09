package com.example.chris.baking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private URL mRecipeLocation;

    public RecipeCardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecipeCardAdapter(this, this);
        recyclerView.setAdapter(mAdapter);

        try {
            mRecipeLocation = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (mRecipeLocation != null){

            AsyncJSONRetrieverCompleted listener = new AsyncJSONRetrieverCompleted() {
                @Override
                public void onTaskComplete(Object result) {

                    mAdapter.setRecipeList((List<Recipe>) result);
                    mAdapter.notifyDataSetChanged();
                }
            };

            new AsyncJSONRetriever(mRecipeLocation, listener).execute();
        }


    }


}
