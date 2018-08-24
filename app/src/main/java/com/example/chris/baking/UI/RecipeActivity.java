package com.example.chris.baking.UI;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.baking.DataTypes.Ingredient;
import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.R;
import com.example.chris.baking.Widget.UpdateWidgetService;

import java.io.StringWriter;

public class RecipeActivity extends AppCompatActivity {

    public static final Recipe SELECTED_RECIPE = new Recipe();

    private int mDefaultPosition = -1;

    private Boolean mTwoPaneView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        if (savedInstanceState != null) {
            savedInstanceState.getParcelable(getString(R.string.intent_extra_recipe));
        } else {
            if(!getIntent().hasExtra(getString(R.string.intent_extra_recipe))){
                NavUtils.navigateUpFromSameTask(this);
                Toast.makeText(getApplicationContext(),getString(R.string.error_no_recipe), Toast.LENGTH_SHORT).show();
                return;
            }
            SELECTED_RECIPE.setRecipe((Recipe) getIntent().getParcelableExtra(getString(R.string.intent_extra_recipe)));
        }
        if (findViewById(R.id.tablet_divider) != null){
            mTwoPaneView = true;
        }

        UpdateWidgetService.startActionUpdateRecipe(this, SELECTED_RECIPE);




        this.setTitle(SELECTED_RECIPE.getName());


        TextView tv = findViewById(R.id.ingredients_tv);
        StringWriter ingredients = new StringWriter();
        for (Ingredient ingredient : SELECTED_RECIPE.getIngredients()) {
            String ingredientString = Ingredient.ingredientToString(ingredient);
            ingredients.append(ingredientString);
        }
        tv.setText(ingredients.toString());
        //closing a StringWriter has no effect


        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();



        for (int i = 0; i < SELECTED_RECIPE.getSteps().size(); i++) {
            String tag = "id_" + i;

            if (fragmentManager.findFragmentByTag(tag) == null) {
                RecipeStepFragment fragment = new RecipeStepFragment();
                fragment.setRecipeInfo(SELECTED_RECIPE.getSteps().get(i), i, mTwoPaneView);
                transaction.add(R.id.recipe_step_fragment_container, fragment, tag);
            }
        }

        if (findViewById(R.id.tablet_divider) != null && savedInstanceState == null) {
            mTwoPaneView = true;

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setFragmentRecipeInfo(SELECTED_RECIPE, 0);

            transaction.add(R.id.detail_fragment_container, detailFragment, "DetailFragment");
        }

        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.intent_extra_recipe), SELECTED_RECIPE);
    }
}
