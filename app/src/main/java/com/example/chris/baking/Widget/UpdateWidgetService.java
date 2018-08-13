package com.example.chris.baking.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.UI.RecipeActivity;

public class UpdateWidgetService extends IntentService {

    public static final String ACTION_UPDATE_WIDGET_INGREDIENTS = "com.example.chris.baking.action.update_widget_ingredients";


    public UpdateWidgetService(){
        super("UpdateWidgetService");
    }


    public static void startActionUpdateRecipe (Context context){
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET_INGREDIENTS);
        //Allows starting a service without app running
        ContextCompat.startForegroundService(context, intent);
    }

    public static void startActionUpdateRecipe (Context context, Recipe recipe){
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET_INGREDIENTS);
        //TODO string resource
        intent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET_INGREDIENTS.equals(action)){
                Recipe recipe = intent.getParcelableExtra(RecipeActivity.EXTRA_RECIPE);
                handleActionUpdateWidget(recipe);
            }
        }

    }

    private void handleActionUpdateWidget(@Nullable Recipe recipe){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));

        BakingAppWidget.updateRecipe(this, appWidgetManager, appIds, recipe);

    }


}
