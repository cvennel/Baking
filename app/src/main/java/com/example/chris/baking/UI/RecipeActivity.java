package com.example.chris.baking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.StringWriter;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final Recipe SELECTED_RECIPE = new Recipe();


    private int mDefaultPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        SELECTED_RECIPE.setRecipe((Recipe) getIntent().getParcelableExtra(EXTRA_RECIPE));

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

            for (int i = 0; i < SELECTED_RECIPE.getRecipieSteps().size(); i++) {
                String tag = "id_" +i;

                if(fragmentManager.findFragmentByTag(tag) == null) {
                    RecipeStepFragment fragment = new RecipeStepFragment();
                    fragment.setRecipeInfo(SELECTED_RECIPE.getRecipieSteps().get(i), i);
                    transaction.add(R.id.recipe_step_fragment_container, fragment, tag);
                }
            }
        transaction.commit();


    }


}
