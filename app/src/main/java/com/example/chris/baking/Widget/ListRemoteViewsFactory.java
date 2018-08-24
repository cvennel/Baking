package com.example.chris.baking.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.chris.baking.DataTypes.Ingredient;
import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.R;

import java.util.List;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Recipe mRecipe;
    List<Ingredient> mIngredientList;


    ListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        if (intent.hasExtra(context.getString(R.string.intent_extra_recipe))) {
            mRecipe = intent.getParcelableExtra(context.getString(R.string.intent_extra_recipe));
            mIngredientList = mRecipe.getIngredients();
        } else {
            mRecipe = null;
            mIngredientList = null;
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mRecipe = BakingAppWidgetProvider.getRecipe();
        mIngredientList = mRecipe.getIngredients();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mRecipe.getIngredients() == null) {
            return 0;
        } else {
            return mRecipe.getIngredients().size();
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);
        views.setTextViewText(R.id.widget_ingredient_item_tv, Ingredient.ingredientToString(mIngredientList.get(i)));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
