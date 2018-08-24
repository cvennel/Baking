package com.example.chris.baking.Widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Adapter;
import android.widget.RemoteViews;

import com.example.chris.baking.DataTypes.Ingredient;
import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.R;
import com.example.chris.baking.UI.MainActivity;
import com.example.chris.baking.UI.RecipeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    private static Recipe mRecipe = new Recipe();

    public static Recipe getRecipe(){
        return mRecipe;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, @Nullable Recipe recipe) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);


        Intent intent = new Intent(context, ListWidgetService.class);


        if (recipe != null) {
            mRecipe = recipe;
            views.setTextViewText(R.id.appwidget_title, mRecipe.getName());
            intent.putExtra(context.getString(R.string.intent_extra_recipe), mRecipe);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));



        }

        views.setRemoteAdapter(R.id.widget_list_view, intent);

        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);



        PendingIntent pendingIntent;
//        Set to open the recipe if provided, otherwise open MainActivity
        if (recipe != null) {
            intent  = new Intent(context, RecipeActivity.class);
            intent.putExtra(context.getString(R.string.intent_extra_recipe), recipe);


            //Creating the back stack to allow navigation to MainActivity.
            // https://developer.android.com/training/implementing-navigation/temporal#java
            pendingIntent = TaskStackBuilder.create(context)
                    .addParentStack(MainActivity.class)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {
            intent = new Intent(context, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        }


        views.setOnClickPendingIntent(R.id.appwidget_container, pendingIntent);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_list_view);
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

