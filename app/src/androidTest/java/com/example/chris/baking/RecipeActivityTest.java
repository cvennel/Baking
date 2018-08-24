package com.example.chris.baking;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;

import com.example.chris.baking.DataTypes.Ingredient;
import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.UI.DetailActivity;
import com.example.chris.baking.UI.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class RecipeActivityTest {

    static final String RECIPE_TITLE = "Nutella Pie";

    private Recipe SAMPLE_RECIPE = SampleRecipe.setupSampleRecipe();

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<RecipeActivity>(RecipeActivity.class){

        //Adding extras to intents tutorial:
        //        https://xebia.com/blog/android-intent-extras-espresso-rules/
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, RecipeActivity.class);
            result.putExtra(targetContext.getString(R.string.intent_extra_recipe), SAMPLE_RECIPE);
            return result;
        }
    };





    //Checks that RecipeActivity retrieves the Recipe from the Intent.Extra correctly and binds to view
    @Test
    public void checkRecipeBinding(){


        StringBuilder stringBuilder = new StringBuilder();
        for(Ingredient ingredient: SAMPLE_RECIPE.getIngredients()){
            stringBuilder.append(Ingredient.ingredientToString(ingredient));
        }

        onView(withId(R.id.ingredients_tv)).check(matches(withText(stringBuilder.toString())));
    }



    //Checks clicking on a recipe step populates detail activity correctly
    @Test
    public void clickRecipeStep(){

        List<Fragment> fragments = mActivityTestRule.getActivity().getSupportFragmentManager().getFragments();

        onView(withId(fragments.get(0).getId())).perform(click());


        onView(withId(R.id.detail_activity_description_tv)).check(matches(withText(SAMPLE_RECIPE.getSteps().get(0).getDescription())));

    }

}
