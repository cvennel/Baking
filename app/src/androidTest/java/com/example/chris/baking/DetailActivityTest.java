package com.example.chris.baking;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.UI.DetailActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    private Recipe SAMPLE_RECIPE = SampleRecipe.setupSampleRecipe();

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule =
            new ActivityTestRule<DetailActivity>(DetailActivity.class){

        @Override
        protected Intent getActivityIntent() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(context, DetailActivity.class);
            result.putExtra(context.getString(R.string.intent_extra_recipe), SAMPLE_RECIPE);
            result.putExtra(DetailActivity.EXTRA_STEP, 0);
            return result;
        }
    };


    @Test
    public void TestButtons(){

        for (int i = 0; i < SAMPLE_RECIPE.getSteps().size(); i++) {
            onView(ViewMatchers.withId(R.id.button_next)).perform(ViewActions.scrollTo());

            onView(ViewMatchers.withId(R.id.detail_activity_description_tv))
                    .check(matches(ViewMatchers
                            .withText(SAMPLE_RECIPE.getSteps().get(i).getDescription())));

            onView(ViewMatchers.withId(R.id.button_next)).perform(ViewActions.click());
        }

//        Checking if toast displayed from:
//          https://stackoverflow.com/questions/28390574/checking-toast-message-in-android-espresso

        onView(ViewMatchers.withText(R.string.error_at_last_step))
                .inRoot(withDecorView(Matchers
                        .not(mActivityTestRule.getActivity()
                                .getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        for (int i = SAMPLE_RECIPE.getSteps().size() -1 ; i >= 0 ; i--) {
            onView(ViewMatchers.withId(R.id.button_previous)).perform(ViewActions.scrollTo());

            onView(ViewMatchers.withId(R.id.detail_activity_description_tv))
                    .check(matches(ViewMatchers
                            .withText(SAMPLE_RECIPE.getSteps().get(i).getDescription())));

            onView(ViewMatchers.withId(R.id.button_previous)).perform(ViewActions.click());
        }

        onView(ViewMatchers.withText(R.string.error_at_first_step))
                .inRoot(withDecorView(Matchers
                        .not(mActivityTestRule.getActivity()
                                .getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

}
