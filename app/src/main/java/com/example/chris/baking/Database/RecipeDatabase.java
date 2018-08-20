package com.example.chris.baking.DataTypes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = Recipe.class, version = 1)
public abstract class RecipeDatabase extends RoomDatabase{

    public abstract RecipeDao recipeDao();

    private static RecipeDatabase INSTANCE;

    public static RecipeDatabase getDatabase(final Context context){

        if (INSTANCE == null){
            synchronized (RecipeDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                            , RecipeDatabase.class, "recipe_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
