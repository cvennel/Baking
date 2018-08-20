package com.example.chris.baking.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class RecipeDatabase_Impl extends RecipeDatabase {
  private volatile RecipeDao _recipeDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `recipe_table` (`id` INTEGER NOT NULL, `name` TEXT, `ingredients` TEXT, `steps` TEXT, `servings` INTEGER NOT NULL, `image` TEXT, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"1bab55105346431a35479de28549e960\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `recipe_table`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsRecipeTable = new HashMap<String, TableInfo.Column>(6);
        _columnsRecipeTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsRecipeTable.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsRecipeTable.put("ingredients", new TableInfo.Column("ingredients", "TEXT", false, 0));
        _columnsRecipeTable.put("steps", new TableInfo.Column("steps", "TEXT", false, 0));
        _columnsRecipeTable.put("servings", new TableInfo.Column("servings", "INTEGER", true, 0));
        _columnsRecipeTable.put("image", new TableInfo.Column("image", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecipeTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecipeTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecipeTable = new TableInfo("recipe_table", _columnsRecipeTable, _foreignKeysRecipeTable, _indicesRecipeTable);
        final TableInfo _existingRecipeTable = TableInfo.read(_db, "recipe_table");
        if (! _infoRecipeTable.equals(_existingRecipeTable)) {
          throw new IllegalStateException("Migration didn't properly handle recipe_table(com.example.chris.baking.DataTypes.Recipe).\n"
                  + " Expected:\n" + _infoRecipeTable + "\n"
                  + " Found:\n" + _existingRecipeTable);
        }
      }
    }, "1bab55105346431a35479de28549e960", "f9edbb94b2524523800bb34bc77e568f");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "recipe_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `recipe_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public RecipeDao recipeDao() {
    if (_recipeDao != null) {
      return _recipeDao;
    } else {
      synchronized(this) {
        if(_recipeDao == null) {
          _recipeDao = new RecipeDao_Impl(this);
        }
        return _recipeDao;
      }
    }
  }
}