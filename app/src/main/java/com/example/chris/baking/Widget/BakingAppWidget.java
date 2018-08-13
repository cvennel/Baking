package com.example.chris.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.chris.baking.DataTypes.Ingredient;
import com.example.chris.baking.UI.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final List<Ingredient> INGREDIENT_LIST= new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
//
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent,0);
        views.setOnClickPendingIntent(R.id.appwidget_container, pendingIntent);


//        if (INGREDIENT_LIST.size() != 0){
//            StringBuilder ingredientsString = new StringBuilder();
//            for (Ingredient ingredient: INGREDIENT_LIST) {
//                ingredientsString.append(Ingredient.ingredientToString(ingredient));
//            }
//            views.setTextViewText(R.id.appwidget_text, ingredientsString);
//        } else {
//            //TODO remove me
//            views.setTextViewText(R.id.appwidget_text, widgetText);
//        }

        views.setTextViewText(R.id.appwidget_text, widgetText);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

    public void setIngredientList(List<Ingredient> list){
        INGREDIENT_LIST.clear();
        INGREDIENT_LIST.addAll(list);
    }
}

