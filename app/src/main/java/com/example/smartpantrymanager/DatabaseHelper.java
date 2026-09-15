package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // -------------------------------------------------
    // DATABASE
    // -------------------------------------------------

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    // -------------------------------------------------
    // PANTRY TABLE
    // -------------------------------------------------

    public static final String TABLE_PANTRY = "pantry_items";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";
    public static final String COLUMN_LOW_STOCK_THRESHOLD =
            "low_stock_threshold";

    // -------------------------------------------------
    // RECIPE TABLE
    // -------------------------------------------------

    public static final String TABLE_RECIPES = "recipes";

    public static final String RECIPE_COLUMN_ID = "id";
    public static final String RECIPE_COLUMN_NAME = "name";
    public static final String RECIPE_COLUMN_INGREDIENTS = "ingredients";
    public static final String RECIPE_COLUMN_METHOD = "method";

    // -------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------

    public DatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // =================================================
    // PANTRY CRUD
    // =================================================

    // -------------------------------------------------
    // CREATE PANTRY ITEM
    // -------------------------------------------------

    public long addPantryItem(PantryItem item) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                item.getName()
        );

        values.put(
                COLUMN_QUANTITY,
                item.getQuantity()
        );

        values.put(
                COLUMN_UNIT,
                item.getUnit()
        );

        values.put(
                COLUMN_CATEGORY,
                item.getCategory()
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                item.getExpiryDate()
        );

        values.put(
                COLUMN_LOW_STOCK_THRESHOLD,
                item.getLowStockThreshold()
        );

        long result =
                db.insert(
                        TABLE_PANTRY,
                        null,
                        values
                );

        db.close();

        return result;
    }

    // -------------------------------------------------
    // READ ALL PANTRY ITEMS
    // -------------------------------------------------

    public ArrayList<PantryItem> getAllPantryItems() {

        ArrayList<PantryItem> pantryItems =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_PANTRY,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COLUMN_NAME + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_NAME
                                )
                        );

                int quantity =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_QUANTITY
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_UNIT
                                )
                        );

                String category =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_CATEGORY
                                )
                        );

                String expiryDate =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_EXPIRY_DATE
                                )
                        );

                int lowStockThreshold =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_LOW_STOCK_THRESHOLD
                                )
                        );

                PantryItem item =
                        new PantryItem(
                                name,
                                quantity,
                                unit,
                                category,
                                expiryDate,
                                lowStockThreshold
                        );

                item.setId(id);

                pantryItems.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pantryItems;
    }

    // -------------------------------------------------
    // UPDATE PANTRY ITEM
    // -------------------------------------------------

    public int updatePantryItem(
            int id,
            PantryItem item) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                item.getName()
        );

        values.put(
                COLUMN_QUANTITY,
                item.getQuantity()
        );

        values.put(
                COLUMN_UNIT,
                item.getUnit()
        );

        values.put(
                COLUMN_CATEGORY,
                item.getCategory()
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                item.getExpiryDate()
        );

        values.put(
                COLUMN_LOW_STOCK_THRESHOLD,
                item.getLowStockThreshold()
        );

        int rowsAffected =
                db.update(
                        TABLE_PANTRY,
                        values,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return rowsAffected;
    }

    // -------------------------------------------------
    // DELETE PANTRY ITEM
    // -------------------------------------------------

    public int deletePantryItem(int id) {

        SQLiteDatabase db =
                getWritableDatabase();

        int rowsDeleted =
                db.delete(
                        TABLE_PANTRY,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return rowsDeleted;
    }

    // =================================================
    // RECIPE METHODS
    // =================================================

    // -------------------------------------------------
    // ADD RECIPE
    // -------------------------------------------------

    private long addRecipe(
            SQLiteDatabase db,
            String name,
            String ingredients,
            String method) {

        ContentValues values =
                new ContentValues();

        values.put(
                RECIPE_COLUMN_NAME,
                name
        );

        values.put(
                RECIPE_COLUMN_INGREDIENTS,
                ingredients
        );

        values.put(
                RECIPE_COLUMN_METHOD,
                method
        );

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }

    // -------------------------------------------------
    // READ ALL RECIPES
    // -------------------------------------------------

    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipes =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_RECIPES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        RECIPE_COLUMN_NAME + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        RECIPE_COLUMN_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        RECIPE_COLUMN_NAME
                                )
                        );

                String ingredients =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        RECIPE_COLUMN_INGREDIENTS
                                )
                        );

                String method =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        RECIPE_COLUMN_METHOD
                                )
                        );

                Recipe recipe =
                        new Recipe(
                                id,
                                name,
                                ingredients,
                                method
                        );

                recipes.add(recipe);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recipes;
    }

    // =================================================
    // DATABASE CREATION
    // =================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        createPantryTable(db);

        createRecipeTable(db);

        seedRecipes(db);
    }

    // -------------------------------------------------
    // CREATE PANTRY TABLE
    // -------------------------------------------------

    private void createPantryTable(
            SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE "
                        + TABLE_PANTRY
                        + " ("
                        + COLUMN_ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_NAME
                        + " TEXT NOT NULL, "
                        + COLUMN_QUANTITY
                        + " INTEGER NOT NULL, "
                        + COLUMN_UNIT
                        + " TEXT NOT NULL, "
                        + COLUMN_CATEGORY
                        + " TEXT, "
                        + COLUMN_EXPIRY_DATE
                        + " TEXT, "
                        + COLUMN_LOW_STOCK_THRESHOLD
                        + " INTEGER DEFAULT 0"
                        + ")";

        db.execSQL(
                createPantryTable
        );
    }

    // -------------------------------------------------
    // CREATE RECIPE TABLE
    // -------------------------------------------------

    private void createRecipeTable(
            SQLiteDatabase db) {

        String createRecipeTable =
                "CREATE TABLE IF NOT EXISTS "
                        + TABLE_RECIPES
                        + " ("
                        + RECIPE_COLUMN_ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + RECIPE_COLUMN_NAME
                        + " TEXT NOT NULL, "
                        + RECIPE_COLUMN_INGREDIENTS
                        + " TEXT NOT NULL, "
                        + RECIPE_COLUMN_METHOD
                        + " TEXT NOT NULL"
                        + ")";

        db.execSQL(
                createRecipeTable
        );
    }

    // =================================================
    // SEED RECIPES
    // =================================================

    private void seedRecipes(
            SQLiteDatabase db) {

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM "
                                + TABLE_RECIPES,
                        null
                );

        int recipeCount = 0;

        if (cursor.moveToFirst()) {

            recipeCount =
                    cursor.getInt(0);
        }

        cursor.close();

        // Do not seed again if recipes already exist
        if (recipeCount > 0) {
            return;
        }

        // Format:
        // quantity|unit|ingredient
        // Multiple ingredients separated with ;

        addRecipe(
                db,
                "Scrambled Eggs",
                "2|items|egg;1|tablespoon|butter",
                "Crack the eggs into a bowl and whisk. "
                        + "Melt butter in a pan. "
                        + "Add eggs and stir gently until cooked."
        );

        addRecipe(
                db,
                "Fried Eggs",
                "2|items|egg;1|tablespoon|oil",
                "Heat oil in a pan. "
                        + "Crack the eggs into the pan and cook until done."
        );

        addRecipe(
                db,
                "Boiled Eggs",
                "2|items|egg;500|ml|water",
                "Place eggs in a pot with water. "
                        + "Bring to the boil and cook until desired firmness."
        );

        addRecipe(
                db,
                "Egg Toast",
                "2|items|egg;2|slices|bread;1|tablespoon|butter",
                "Cook the eggs in a pan. "
                        + "Toast the bread and spread with butter. "
                        + "Serve the eggs on the toast."
        );

        addRecipe(
                db,
                "Cheese Toast",
                "2|slices|bread;2|slices|cheese",
                "Place cheese between the bread slices. "
                        + "Toast until the bread is golden and the cheese melts."
        );

        addRecipe(
                db,
                "Tomato Toast",
                "2|slices|bread;1|item|tomato;1|tablespoon|butter",
                "Toast the bread and spread with butter. "
                        + "Slice the tomato and place it on top."
        );

        addRecipe(
                db,
                "Banana Toast",
                "2|slices|bread;1|item|banana",
                "Toast the bread. "
                        + "Slice the banana and place it on top."
        );

        addRecipe(
                db,
                "Simple Oats",
                "1|cup|oats;2|cups|milk",
                "Combine oats and milk in a pot. "
                        + "Cook while stirring until thick and creamy."
        );

        addRecipe(
                db,
                "Banana Oats",
                "1|cup|oats;2|cups|milk;1|item|banana",
                "Cook oats with milk until soft. "
                        + "Slice the banana and add it before serving."
        );

        addRecipe(
                db,
                "Rice and Egg",
                "1|cup|rice;2|items|egg;1|tablespoon|oil",
                "Cook the rice. "
                        + "Fry the eggs in oil and serve them with the rice."
        );

        addRecipe(
                db,
                "Tomato Rice",
                "1|cup|rice;2|items|tomato;1|tablespoon|oil",
                "Cook the rice. "
                        + "Chop tomatoes and cook them in oil. "
                        + "Mix with the cooked rice."
        );

        addRecipe(
                db,
                "Cheesy Rice",
                "1|cup|rice;2|slices|cheese",
                "Cook the rice until tender. "
                        + "Add cheese while the rice is hot and stir until melted."
        );

        addRecipe(
                db,
                "Chicken and Rice",
                "1|cup|rice;2|pieces|chicken;1|tablespoon|oil",
                "Cook the rice. "
                        + "Cook the chicken in oil until fully done. "
                        + "Serve with the rice."
        );

        addRecipe(
                db,
                "Chicken Sandwich",
                "2|slices|bread;1|piece|chicken;1|item|tomato",
                "Cook the chicken thoroughly. "
                        + "Slice the chicken and tomato. "
                        + "Place them between the bread slices."
        );

        addRecipe(
                db,
                "Tomato Omelette",
                "2|items|egg;1|item|tomato;1|tablespoon|oil",
                "Whisk the eggs. "
                        + "Chop the tomato. "
                        + "Heat oil in a pan, add tomato and eggs, "
                        + "then cook until set."
        );

        addRecipe(
                db,
                "Cheese Omelette",
                "2|items|egg;2|slices|cheese;1|tablespoon|oil",
                "Whisk the eggs and pour into a heated oiled pan. "
                        + "Add cheese and fold the omelette when cooked."
        );

        addRecipe(
                db,
                "Banana Milkshake",
                "1|item|banana;2|cups|milk",
                "Peel and slice the banana. "
                        + "Blend with milk until smooth."
        );

        addRecipe(
                db,
                "Simple Pasta",
                "1|cup|pasta;500|ml|water;1|tablespoon|oil",
                "Boil the pasta in water until tender. "
                        + "Drain and mix with oil."
        );

        addRecipe(
                db,
                "Tomato Pasta",
                "1|cup|pasta;2|items|tomato;1|tablespoon|oil",
                "Cook the pasta. "
                        + "Chop and cook tomatoes in oil. "
                        + "Mix the tomato mixture with the pasta."
        );

        addRecipe(
                db,
                "Cheesy Pasta",
                "1|cup|pasta;2|slices|cheese",
                "Cook and drain the pasta. "
                        + "Add cheese while hot and stir until melted."
        );
    }

    // =================================================
    // DATABASE UPGRADE
    // =================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        // Version 1 only had the pantry table.
        // Version 2 introduces recipes.
        // We keep the existing pantry data.

        if (oldVersion < 2) {

            createRecipeTable(db);

            seedRecipes(db);
        }
    }
}