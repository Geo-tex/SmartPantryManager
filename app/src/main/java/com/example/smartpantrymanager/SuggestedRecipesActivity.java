package com.example.smartpantrymanager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerSuggestedRecipes;
    private TextView textRecipeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);

        databaseHelper =
                new DatabaseHelper(this);

        recyclerSuggestedRecipes =
                findViewById(
                        R.id.recyclerSuggestedRecipes
                );

        textRecipeMessage =
                findViewById(
                        R.id.textRecipeMessage
                );

        recyclerSuggestedRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {

        ArrayList<Recipe> allRecipes =
                databaseHelper.getAllRecipes();

        Log.d(
                "RECIPES_TEST",
                "Recipes in database: "
                        + allRecipes.size()
        );

        ArrayList<PantryItem> pantryItems =
                databaseHelper.getAllPantryItems();

        Log.d(
                "RECIPES_TEST",
                "Pantry items: "
                        + pantryItems.size()
        );

        ArrayList<Recipe> suggestedRecipes =
                new ArrayList<>();

        ArrayList<String> recipeStatuses =
                new ArrayList<>();

        /*
         * A recipe is suggested ONLY when every required
         * ingredient is available in the pantry in at least
         * the required quantity.
         *
         * Partial matches are intentionally excluded.
         */
        for (Recipe recipe : allRecipes) {

            ArrayList<String> missingIngredients =
                    getMissingIngredients(
                            recipe,
                            pantryItems
                    );

            Log.d(
                    "RECIPES_TEST",
                    recipe.getName()
                            + " missing ingredients: "
                            + missingIngredients.size()
            );

            if (missingIngredients.isEmpty()) {

                suggestedRecipes.add(
                        recipe
                );

                recipeStatuses.add(
                        "Ready to make"
                );
            }
        }

        Log.d(
                "RECIPES_TEST",
                "Recipes ready to make: "
                        + suggestedRecipes.size()
        );

        RecipeAdapter recipeAdapter =
                new RecipeAdapter(
                        suggestedRecipes,
                        recipeStatuses
                );

        recyclerSuggestedRecipes.setAdapter(
                recipeAdapter
        );

        if (suggestedRecipes.isEmpty()) {

            textRecipeMessage.setText(
                    "No recipes can be made with your current pantry ingredients."
            );

        } else if (suggestedRecipes.size() == 1) {

            textRecipeMessage.setText(
                    "1 recipe ready to make"
            );

        } else {

            textRecipeMessage.setText(
                    suggestedRecipes.size()
                            + " recipes ready to make"
            );
        }
    }

    private ArrayList<String> getMissingIngredients(
            Recipe recipe,
            ArrayList<PantryItem> pantryItems) {

        ArrayList<String> missingIngredients =
                new ArrayList<>();

        String ingredientsText =
                recipe.getIngredients();

        if (ingredientsText == null
                || ingredientsText.trim().isEmpty()) {

            return missingIngredients;
        }

        String[] requiredIngredients =
                ingredientsText.split(";");

        for (String requiredIngredient
                : requiredIngredients) {

            String[] parts =
                    requiredIngredient.split("\\|");

            if (parts.length != 3) {

                continue;
            }

            double requiredQuantity;

            try {

                requiredQuantity =
                        Double.parseDouble(
                                parts[0]
                        );

            } catch (NumberFormatException e) {

                continue;
            }

            String requiredUnit =
                    normalizeUnit(
                            parts[1]
                    );

            String requiredName =
                    normalizeIngredientName(
                            parts[2]
                    );

            String displayName =
                    parts[2].trim();

            boolean ingredientFound =
                    false;

            for (PantryItem pantryItem
                    : pantryItems) {

                String pantryName =
                        normalizeIngredientName(
                                pantryItem.getName()
                        );

                if (!pantryName.equals(
                        requiredName)) {

                    continue;
                }

                String pantryUnit =
                        normalizeUnit(
                                pantryItem.getUnit()
                        );

                double pantryQuantity =
                        pantryItem.getQuantity();

                if (hasEnoughQuantity(
                        pantryQuantity,
                        pantryUnit,
                        requiredQuantity,
                        requiredUnit)) {

                    ingredientFound =
                            true;

                    break;
                }
            }

            if (!ingredientFound) {

                missingIngredients.add(
                        displayName
                );
            }
        }

        return missingIngredients;
    }

    private boolean hasEnoughQuantity(
            double pantryQuantity,
            String pantryUnit,
            double requiredQuantity,
            String requiredUnit) {

        if (pantryUnit.equals(
                requiredUnit)) {

            return pantryQuantity
                    >= requiredQuantity;
        }

        Double convertedPantryQuantity =
                convertQuantity(
                        pantryQuantity,
                        pantryUnit,
                        requiredUnit
                );

        if (convertedPantryQuantity
                == null) {

            return false;
        }

        return convertedPantryQuantity
                >= requiredQuantity;
    }

    private Double convertQuantity(
            double quantity,
            String fromUnit,
            String toUnit) {

        // WEIGHT CONVERSIONS

        if (fromUnit.equals("kg")
                && toUnit.equals("g")) {

            return quantity * 1000.0;
        }

        if (fromUnit.equals("g")
                && toUnit.equals("kg")) {

            return quantity / 1000.0;
        }

        // LIQUID CONVERSIONS

        if (fromUnit.equals("litre")
                && toUnit.equals("ml")) {

            return quantity * 1000.0;
        }

        if (fromUnit.equals("ml")
                && toUnit.equals("litre")) {

            return quantity / 1000.0;
        }

        // LITRE AND CUP CONVERSIONS

        if (fromUnit.equals("litre")
                && toUnit.equals("cup")) {

            return quantity
                    * 1000.0
                    / 240.0;
        }

        if (fromUnit.equals("cup")
                && toUnit.equals("litre")) {

            return quantity
                    * 240.0
                    / 1000.0;
        }

        // ML AND CUP CONVERSIONS

        if (fromUnit.equals("ml")
                && toUnit.equals("cup")) {

            return quantity
                    / 240.0;
        }

        if (fromUnit.equals("cup")
                && toUnit.equals("ml")) {

            return quantity
                    * 240.0;
        }

        return null;
    }

    private String normalizeIngredientName(
            String text) {

        String value =
                normalizeText(text);

        /*
         * Basic singularisation so pantry names such as
         * "eggs" can match recipe ingredient "egg".
         */

        if (value.endsWith("ies")
                && value.length() > 3) {

            value =
                    value.substring(
                            0,
                            value.length() - 3
                    ) + "y";

        } else if (value.endsWith("oes")
                && value.length() > 3) {

            value =
                    value.substring(
                            0,
                            value.length() - 2
                    );

        } else if (value.endsWith("s")
                && value.length() > 1
                && !value.endsWith("ss")) {

            value =
                    value.substring(
                            0,
                            value.length() - 1
                    );
        }

        return value;
    }

    private String normalizeUnit(
            String unit) {

        unit =
                normalizeText(unit);

        if (unit.equals("item")
                || unit.equals("items")) {

            return "item";
        }

        if (unit.equals("slice")
                || unit.equals("slices")) {

            return "slice";
        }

        if (unit.equals("piece")
                || unit.equals("pieces")) {

            return "piece";
        }

        if (unit.equals("cup")
                || unit.equals("cups")) {

            return "cup";
        }

        if (unit.equals("tablespoon")
                || unit.equals("tablespoons")
                || unit.equals("tbsp")
                || unit.equals("tbs")) {

            return "tablespoon";
        }

        if (unit.equals("ml")
                || unit.equals("millilitre")
                || unit.equals("millilitres")
                || unit.equals("milliliter")
                || unit.equals("milliliters")) {

            return "ml";
        }

        if (unit.equals("l")
                || unit.equals("litre")
                || unit.equals("litres")
                || unit.equals("liter")
                || unit.equals("liters")) {

            return "litre";
        }

        if (unit.equals("g")
                || unit.equals("gram")
                || unit.equals("grams")) {

            return "g";
        }

        if (unit.equals("kg")
                || unit.equals("kilogram")
                || unit.equals("kilograms")) {

            return "kg";
        }

        return unit;
    }

    private String normalizeText(
            String text) {

        if (text == null) {

            return "";
        }

        return text
                .trim()
                .toLowerCase();
    }
}