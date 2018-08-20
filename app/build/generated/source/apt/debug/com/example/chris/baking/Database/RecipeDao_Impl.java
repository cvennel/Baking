package com.example.chris.baking.Database;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import com.example.chris.baking.DataTypes.Ingredient;
import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.DataTypes.RecipeStep;
import com.example.chris.baking.Utils.ListTypeConverter;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class RecipeDao_Impl implements RecipeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfRecipe;

  private final SharedSQLiteStatement __preparedStmtOfClearRecipeDB;

  public RecipeDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecipe = new EntityInsertionAdapter<Recipe>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `recipe_table`(`id`,`name`,`ingredients`,`steps`,`servings`,`image`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Recipe value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        final String _tmp;
        _tmp = ListTypeConverter.IngredientToString(value.getIngredients());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
        final String _tmp_1;
        _tmp_1 = ListTypeConverter.recipeStepToString(value.getSteps());
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp_1);
        }
        stmt.bindLong(5, value.getServings());
        if (value.getImage() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getImage());
        }
      }
    };
    this.__preparedStmtOfClearRecipeDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM recipe_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(Recipe recipe) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfRecipe.insert(recipe);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void clearRecipeDB() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfClearRecipeDB.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfClearRecipeDB.release(_stmt);
    }
  }

  @Override
  public List<Recipe> getAllRecipe() {
    final String _sql = "SELECT * from recipe_table ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfIngredients = _cursor.getColumnIndexOrThrow("ingredients");
      final int _cursorIndexOfSteps = _cursor.getColumnIndexOrThrow("steps");
      final int _cursorIndexOfServings = _cursor.getColumnIndexOrThrow("servings");
      final int _cursorIndexOfImage = _cursor.getColumnIndexOrThrow("image");
      final List<Recipe> _result = new ArrayList<Recipe>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Recipe _item;
        _item = new Recipe();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final List<Ingredient> _tmpIngredients;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfIngredients);
        _tmpIngredients = ListTypeConverter.stringToIngredient(_tmp);
        _item.setIngredients(_tmpIngredients);
        final List<RecipeStep> _tmpSteps;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfSteps);
        _tmpSteps = ListTypeConverter.stringToRecipeStep(_tmp_1);
        _item.setSteps(_tmpSteps);
        final int _tmpServings;
        _tmpServings = _cursor.getInt(_cursorIndexOfServings);
        _item.setServings(_tmpServings);
        final String _tmpImage;
        _tmpImage = _cursor.getString(_cursorIndexOfImage);
        _item.setImage(_tmpImage);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
