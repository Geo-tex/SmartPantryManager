package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PantryItem selectedItem = null;

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerPantryItems;

    private Button buttonAddEditIngredient;
    private Button buttonDeleteItem;

    private boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /*
         * Prevent content from being hidden behind
         * the status bar and navigation bar.
         */
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, windowInsets) -> {

                    Insets systemBars =
                            windowInsets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return windowInsets;
                }
        );

        databaseHelper =
                new DatabaseHelper(this);

        buttonAddEditIngredient =
                findViewById(
                        R.id.buttonAddEditIngredient
                );

        Button buttonSuggestedRecipes =
                findViewById(
                        R.id.buttonSuggestedRecipes
                );

        Button buttonSettings =
                findViewById(
                        R.id.buttonSettings
                );

        buttonDeleteItem =
                findViewById(
                        R.id.buttonDeleteItem
                );

        recyclerPantryItems =
                findViewById(
                        R.id.recyclerPantryItems
                );

        recyclerPantryItems.setLayoutManager(
                new LinearLayoutManager(this)
        );

        updateSelectionControls();
        refreshPantryItems();

        buttonAddEditIngredient.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddEditIngredientActivity.class
                    );

            if (selectedItem != null) {

                intent.putExtra(
                        "pantryItemId",
                        selectedItem.getId()
                );

                intent.putExtra(
                        "pantryItemName",
                        selectedItem.getName()
                );

                intent.putExtra(
                        "pantryItemQuantity",
                        selectedItem.getQuantity()
                );

                intent.putExtra(
                        "pantryItemUnit",
                        selectedItem.getUnit()
                );

                intent.putExtra(
                        "pantryItemCategory",
                        selectedItem.getCategory()
                );

                intent.putExtra(
                        "pantryItemExpiryDate",
                        selectedItem.getExpiryDate()
                );

                intent.putExtra(
                        "pantryItemLowStock",
                        selectedItem.getLowStockThreshold()
                );
            }

            startActivity(intent);
        });

        buttonSuggestedRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class
                    );

            startActivity(intent);
        });

        buttonSettings.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);
        });

        buttonDeleteItem.setOnClickListener(v -> {

            if (selectedItem == null) {

                Toast.makeText(
                        MainActivity.this,
                        "Please select an ingredient first",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            int rowsDeleted =
                    databaseHelper.deletePantryItem(
                            selectedItem.getId()
                    );

            if (rowsDeleted > 0) {

                Toast.makeText(
                        MainActivity.this,
                        "Ingredient deleted successfully",
                        Toast.LENGTH_SHORT
                ).show();

                selectedItem = null;

                updateSelectionControls();
                refreshPantryItems();

            } else {

                Toast.makeText(
                        MainActivity.this,
                        "Failed to delete ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null
                && recyclerPantryItems != null) {

            if (!firstLoad) {
                selectedItem = null;
            }

            refreshPantryItems();
            updateSelectionControls();

            firstLoad = false;
        }
    }

    private void refreshPantryItems() {

        ArrayList<PantryItem> pantryItems =
                databaseHelper.getAllPantryItems();

        PantryAdapter pantryAdapter =
                new PantryAdapter(
                        MainActivity.this,
                        pantryItems,
                        pantryItem -> {

                            if (selectedItem != null
                                    && selectedItem.getId()
                                    == pantryItem.getId()) {

                                String itemName =
                                        selectedItem.getName();

                                selectedItem = null;

                                updateSelectionControls();

                                Toast.makeText(
                                        MainActivity.this,
                                        itemName + " deselected",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } else {

                                selectedItem = pantryItem;

                                updateSelectionControls();

                                Toast.makeText(
                                        MainActivity.this,
                                        pantryItem.getName()
                                                + " selected",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );

        recyclerPantryItems.setAdapter(
                pantryAdapter
        );
    }

    private void updateSelectionControls() {

        if (selectedItem == null) {

            buttonAddEditIngredient.setText(
                    "Add Ingredient"
            );

            buttonDeleteItem.setEnabled(
                    false
            );

        } else {

            buttonAddEditIngredient.setText(
                    "Edit Selected Ingredient"
            );

            buttonDeleteItem.setEnabled(
                    true
            );
        }
    }
}