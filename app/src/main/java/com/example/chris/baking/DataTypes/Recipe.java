package com.example.chris.baking.DataTypes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<RecipeStep> steps;
    private int servings;
    private String image;

    public Recipe(){

    }
    public Recipe(int id, String name, List<Ingredient> ingredients, List<RecipeStep> recipeSteps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = recipeSteps;
        this.servings = servings;
        this.image = image;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeStep> getRecipieSteps() {
        return steps;
    }

    public void setRecipieSteps(List<RecipeStep> recipeSteps) {
        this.steps = recipeSteps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };

    public Recipe(Parcel parcel){
        id = parcel.readInt();
        name = parcel.readString();
        ingredients = new ArrayList<>();
        parcel.readList(ingredients,Recipe.class.getClassLoader());
        steps = new ArrayList<>();
        parcel.readList(steps, Recipe.class.getClassLoader());
        servings = parcel.readInt();
        image = parcel.readString();
    }

    public void setRecipe(Recipe recipe){
        this.id = recipe.id;
        this.name = recipe.name;
        this.ingredients = recipe.ingredients;
        this.steps = recipe.getRecipieSteps();
        this.servings = recipe.servings;
        this.image = recipe.image;
    }
}

