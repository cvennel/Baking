package com.example.chris.baking;

import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<RecipeStep> steps;
    private int servings;
    private String image;

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
}

