package com.example.chris.baking;

import com.example.chris.baking.DataTypes.Ingredient;
import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.DataTypes.RecipeStep;

import java.util.ArrayList;
import java.util.List;

public class SampleRecipe {

    public static Recipe setupSampleRecipe(){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "cup", "sugar"));
        ingredients.add(new Ingredient(2, "cups", "spice"));


        List<RecipeStep> steps = new ArrayList<>();
        steps.add(new RecipeStep(1, "short description", "description", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "thumbnailURL"));
        steps.add(new RecipeStep(2, "beat it", "beat it good", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "thumbnail"));

        return new Recipe(1, "Sample Recipe", ingredients, steps,9,"image");
    }

}
