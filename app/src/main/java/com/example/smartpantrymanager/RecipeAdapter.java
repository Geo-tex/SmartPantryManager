package com.example.smartpantrymanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final ArrayList<Recipe> recipes;
    private final ArrayList<String> recipeStatuses;

    public RecipeAdapter(
            ArrayList<Recipe> recipes,
            ArrayList<String> recipeStatuses) {

        this.recipes =
                recipes;

        this.recipeStatuses =
                recipeStatuses;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_recipe,
                        parent,
                        false
                );

        return new RecipeViewHolder(
                view
        );
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe =
                recipes.get(
                        position
                );

        holder.textRecipeName.setText(
                recipe.getName()
        );

        if (position
                < recipeStatuses.size()) {

            holder.textRecipeInfo.setText(
                    recipeStatuses.get(
                            position
                    )
            );

        } else {

            holder.textRecipeInfo.setText(
                    "Tap to view ingredients and preparation"
            );
        }

        holder.itemView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            v.getContext(),
                            RecipeDetailActivity.class
                    );

            intent.putExtra(
                    "recipeName",
                    recipe.getName()
            );

            intent.putExtra(
                    "recipeIngredients",
                    recipe.getIngredients()
            );

            intent.putExtra(
                    "recipeMethod",
                    recipe.getMethod()
            );

            v.getContext().startActivity(
                    intent
            );
        });
    }

    @Override
    public int getItemCount() {

        return recipes.size();
    }

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView textRecipeName;
        TextView textRecipeInfo;

        public RecipeViewHolder(
                @NonNull View itemView) {

            super(
                    itemView
            );

            textRecipeName =
                    itemView.findViewById(
                            R.id.textRecipeName
                    );

            textRecipeInfo =
                    itemView.findViewById(
                            R.id.textRecipeInfo
                    );
        }
    }
}