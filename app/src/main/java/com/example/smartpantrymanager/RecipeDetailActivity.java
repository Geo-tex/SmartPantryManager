package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        TextView textRecipeName =
                findViewById(R.id.textRecipeName);

        TextView textRecipeIngredients =
                findViewById(R.id.textRecipeIngredients);

        TextView textRecipeMethod =
                findViewById(R.id.textRecipeMethod);

        String recipeName =
                getIntent().getStringExtra("recipeName");

        String recipeIngredients =
                getIntent().getStringExtra("recipeIngredients");

        String recipeMethod =
                getIntent().getStringExtra("recipeMethod");

        textRecipeName.setText(recipeName);

        textRecipeIngredients.setText(
                formatIngredients(recipeIngredients)
        );

        textRecipeMethod.setText(recipeMethod);
    }

    private String formatIngredients(String ingredientsText) {

        if (ingredientsText == null
                || ingredientsText.trim().isEmpty()) {

            return "No ingredients available.";
        }

        String[] ingredients =
                ingredientsText.split(";");

        StringBuilder formattedIngredients =
                new StringBuilder();

        for (String ingredient : ingredients) {

            String[] parts =
                    ingredient.split("\\|");

            if (parts.length == 3) {

                formattedIngredients
                        .append("• ")
                        .append(parts[0])
                        .append(" ")
                        .append(parts[1])
                        .append(" ")
                        .append(parts[2])
                        .append("\n");
            }
        }

        return formattedIngredients.toString().trim();
    }
}