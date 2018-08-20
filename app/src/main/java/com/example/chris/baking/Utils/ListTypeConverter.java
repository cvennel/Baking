package com.example.chris.baking.Utils;

import android.arch.persistence.room.TypeConverter;

import com.example.chris.baking.DataTypes.Ingredient;
import com.example.chris.baking.DataTypes.RecipeStep;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

//https://medium.com/@toddcookevt/android-room-storing-lists-of-objects-766cca57e3f9
public class RecipeStepListTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<RecipeStep> stringToRecipeStep(String string){
        if (string == null){
            return Collections.emptyList();
        }

        Type recipeStepListType = new TypeToken<List<RecipeStep>>(){}.getType();

        return gson.fromJson(string, recipeStepListType);
    }


    @TypeConverter
    public  static  String recipeStepToString(List<RecipeStep> recipeSteps){
        return gson.toJson(recipeSteps);
    }


    @TypeConverter
    public static List<Ingredient> stringToIngredient(String string){
        if (string == null){
            return Collections.emptyList();
        }

        Type ingredientListType = new TypeToken<List<Ingredient>>(){}.getType();

        return gson.fromJson(string, ingredientListType);
    }


    @TypeConverter
    public  static  String IngredientToString(List<Ingredient> ingredients){
        return gson.toJson(ingredients);
    }
}
