package com.example.chris.baking.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.chris.baking.DataTypes.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    void insert(Recipe recipe);

    @Query("SELECT * from recipe_table ORDER BY id ASC")
    List<Recipe> getAllRecipe();

    @Query("DELETE FROM recipe_table")
    void clearRecipeDB();

}
