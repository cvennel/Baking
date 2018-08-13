package com.example.chris.baking;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.remote.EspressoRemoteMessage;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.chris.baking.UI.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainActivityRecipeCardSelectTest {

    static final String RECIPE_TITLE = "Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);



    @Test
    public void clickRecipeCard_OpensRecipeActivity(){

        onView(withId(R.id.main_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //find a view that a) is a text view and b) has a parent that is a toolbar. The only result should be the title bar
        onView(allOf(Matchers.<View>instanceOf(TextView.class), withParent(Matchers.<View>instanceOf(Toolbar.class)))).check(matches(withText(RECIPE_TITLE)));
    }

}
