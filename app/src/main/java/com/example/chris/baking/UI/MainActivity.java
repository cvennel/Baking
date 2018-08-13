package com.example.chris.baking.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.R;

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
            mRecipeLocation = new URL(getString(R.string.json_url));
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
