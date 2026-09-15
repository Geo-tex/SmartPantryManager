package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TextView textIngredientHeading;

    private EditText editIngredientName;
    private EditText editIngredientQuantity;
    private EditText editIngredientUnit;
    private EditText editIngredientCategory;
    private EditText editIngredientExpiryDate;
    private EditText editIngredientLowStock;

    private Button buttonSaveIngredient;

    private int pantryItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        databaseHelper =
                new DatabaseHelper(this);

        textIngredientHeading =
                findViewById(
                        R.id.textIngredientHeading
                );

        editIngredientName =
                findViewById(
                        R.id.editIngredientName
                );

        editIngredientQuantity =
                findViewById(
                        R.id.editIngredientQuantity
                );

        editIngredientUnit =
                findViewById(
                        R.id.editIngredientUnit
                );

        editIngredientCategory =
                findViewById(
                        R.id.editIngredientCategory
                );

        editIngredientExpiryDate =
                findViewById(
                        R.id.editIngredientExpiryDate
                );

        editIngredientLowStock =
                findViewById(
                        R.id.editIngredientLowStock
                );

        buttonSaveIngredient =
                findViewById(
                        R.id.buttonSaveIngredient
                );

        pantryItemId =
                getIntent().getIntExtra(
                        "pantryItemId",
                        -1
                );

        if (pantryItemId != -1) {

            setTitle("Edit Ingredient");

            textIngredientHeading.setText(
                    "Edit Ingredient"
            );

            editIngredientName.setText(
                    getIntent().getStringExtra(
                            "pantryItemName"
                    )
            );

            editIngredientQuantity.setText(
                    String.valueOf(
                            getIntent().getIntExtra(
                                    "pantryItemQuantity",
                                    0
                            )
                    )
            );

            editIngredientUnit.setText(
                    getIntent().getStringExtra(
                            "pantryItemUnit"
                    )
            );

            editIngredientCategory.setText(
                    getIntent().getStringExtra(
                            "pantryItemCategory"
                    )
            );

            editIngredientExpiryDate.setText(
                    getIntent().getStringExtra(
                            "pantryItemExpiryDate"
                    )
            );

            editIngredientLowStock.setText(
                    String.valueOf(
                            getIntent().getIntExtra(
                                    "pantryItemLowStock",
                                    0
                            )
                    )
            );

            buttonSaveIngredient.setText(
                    "Update Ingredient"
            );

        } else {

            setTitle("Add Ingredient");

            textIngredientHeading.setText(
                    "Add Ingredient"
            );

            buttonSaveIngredient.setText(
                    "Save Ingredient"
            );
        }

        buttonSaveIngredient.setOnClickListener(v ->
                saveIngredient()
        );
    }

    private void saveIngredient() {

        String name =
                editIngredientName.getText()
                        .toString()
                        .trim();

        String quantityText =
                editIngredientQuantity.getText()
                        .toString()
                        .trim();

        String unit =
                editIngredientUnit.getText()
                        .toString()
                        .trim();

        String category =
                editIngredientCategory.getText()
                        .toString()
                        .trim();

        String expiryDate =
                editIngredientExpiryDate.getText()
                        .toString()
                        .trim();

        String lowStockText =
                editIngredientLowStock.getText()
                        .toString()
                        .trim();

        if (name.isEmpty()
                || quantityText.isEmpty()
                || unit.isEmpty()
                || lowStockText.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill in all required fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int quantity;
        int lowStockThreshold;

        try {

            quantity =
                    Integer.parseInt(
                            quantityText
                    );

            lowStockThreshold =
                    Integer.parseInt(
                            lowStockText
                    );

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Quantity and low stock threshold must be numbers",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (quantity < 0
                || lowStockThreshold < 0) {

            Toast.makeText(
                    this,
                    "Quantity and low stock threshold cannot be negative",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!expiryDate.isEmpty()
                && !isValidDate(expiryDate)) {

            Toast.makeText(
                    this,
                    "Expiry date must use YYYY-MM-DD",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        PantryItem pantryItem =
                new PantryItem(
                        name,
                        quantity,
                        unit,
                        category,
                        expiryDate,
                        lowStockThreshold
                );

        if (pantryItemId == -1) {

            long result =
                    databaseHelper.addPantryItem(
                            pantryItem
                    );

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Ingredient saved successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to save ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            int rowsUpdated =
                    databaseHelper.updatePantryItem(
                            pantryItemId,
                            pantryItem
                    );

            if (rowsUpdated > 0) {

                Toast.makeText(
                        this,
                        "Ingredient updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to update ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private boolean isValidDate(
            String dateText) {

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        sdf.setLenient(false);

        try {

            sdf.parse(dateText);

            return true;

        } catch (ParseException e) {

            return false;
        }
    }
}