package com.example.chris.baking.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.R;


public class DetailActivity extends AppCompatActivity {

//    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final String EXTRA_STEP = "extra_step";
    public static final String EXTRA_TWO_PANE = "extra_two_pane";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        FragmentManager manager = getSupportFragmentManager();

        DetailFragment fragment = new DetailFragment();

        Recipe currentRecipe = getIntent().getParcelableExtra(getString(R.string.intent_extra_recipe));
        int currentStepNumber = getIntent().getIntExtra(EXTRA_STEP, -5);

        Boolean isTwoPane = getIntent().getBooleanExtra(EXTRA_TWO_PANE, false);

        fragment.setFragmentRecipeInfo(currentRecipe, currentStepNumber);


        manager.beginTransaction().add(R.id.detail_fragment_container, fragment, "DetailFragment").commit();

    }


}


