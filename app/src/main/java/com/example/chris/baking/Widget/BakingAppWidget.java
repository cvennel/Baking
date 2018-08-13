package com.example.chris.baking.Widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.chris.baking.DataTypes.Ingredient;
import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.R;
import com.example.chris.baking.UI.MainActivity;
import com.example.chris.baking.UI.RecipeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, @Nullable Recipe recipe) {

        CharSequence widgetText = context.getString(R.string.appwidget_title);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
//


        //Set to open the recipe if provided, otherwise open MainActivity
        if (recipe != null) {
            StringBuilder ingredientsString = new StringBuilder();
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredientsString.append(Ingredient.ingredientToString(ingredient));
            }
            views.setTextViewText(R.id.appwidget_title, recipe.getName());
            views.setTextViewText(R.id.appwidget_text, ingredientsString);

            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);

            //Creating the back stack to allow navigation to MainActivity.
            // https://developer.android.com/training/implementing-navigation/temporal#java
            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addParentStack(MainActivity.class)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.appwidget_container, pendingIntent);

        } else {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.appwidget_container, pendingIntent);
        }


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        UpdateWidgetService.startActionUpdateRecipe(context);
    }


    public static void updateRecipe(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, @Nullable Recipe recipe) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

