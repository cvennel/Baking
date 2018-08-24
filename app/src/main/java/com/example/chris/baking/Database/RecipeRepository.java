package com.example.chris.baking.Database;

import android.app.Application;
import android.os.AsyncTask;

import com.example.chris.baking.DataTypes.Recipe;

import java.util.List;

public class RecipeRepository {

    private RecipeDao recipeDao;
    static List<Recipe> mRecipeList;


    public  List<Recipe> getRecipes(){
        return mRecipeList;
    }
    public RecipeRepository(Application application){
        RecipeDatabase db = RecipeDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
        new AsyncGetRecipes(recipeDao).execute();
    }


    public void updateRecipes(List<Recipe> recipes){

        new AsyncClearTask(recipeDao).execute();

        for(Recipe recipe : recipes) {
            new AsyncInsertTask(recipeDao).execute(recipe);
        }
    }

    public void clear(){
        new AsyncClearTask(recipeDao).execute();
    }

    private static class AsyncGetRecipes extends AsyncTask<Void,Void,Void>{

        RecipeDao recipeDao;

        AsyncGetRecipes(RecipeDao dao){
            recipeDao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            mRecipeList = recipeDao.getAllRecipe();
            return null;
        }

    }

    private static class AsyncInsertTask extends AsyncTask<Recipe, Void, Void>{
        private  RecipeDao recipeDao;

        AsyncInsertTask(RecipeDao dao){
            recipeDao = dao;
        }
        @Override
        protected Void doInBackground(Recipe... recipes) {
            recipeDao.insert(recipes[0]);
            return null;
        }
    }

    private static class AsyncClearTask extends AsyncTask<Void, Void, Void>{
        private  RecipeDao recipeDao;

        AsyncClearTask(RecipeDao dao){
            recipeDao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            recipeDao.clearRecipeDB();
            return null;
        }
    }
}
