package com.example.smartpantrymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final ArrayList<PantryItem> pantryItems;
    private final OnItemClickListener listener;
    private final boolean expiryAlertsEnabled;

    public interface OnItemClickListener {
        void onItemClick(PantryItem pantryItem);
    }

    public PantryAdapter(
            Context context,
            ArrayList<PantryItem> pantryItems,
            OnItemClickListener listener) {

        this.pantryItems = pantryItems;
        this.listener = listener;

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(
                        "SmartPantrySettings",
                        Context.MODE_PRIVATE
                );

        expiryAlertsEnabled =
                sharedPreferences.getBoolean(
                        "expiryAlerts",
                        true
                );
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_pantry,
                        parent,
                        false
                );

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {

        PantryItem pantryItem =
                pantryItems.get(position);

        // DISPLAY INGREDIENT NAME
        holder.textPantryName.setText(
                pantryItem.getName()
        );

        // DISPLAY QUANTITY, UNIT AND CATEGORY
        String details =
                pantryItem.getQuantity()
                        + " "
                        + pantryItem.getUnit();

        String category =
                pantryItem.getCategory();

        if (category != null
                && !category.trim().isEmpty()) {

            details =
                    details
                            + " • "
                            + category;
        }

        holder.textPantryDetails.setText(
                details
        );

        // BUILD STATUS MESSAGE
        StringBuilder status =
                new StringBuilder();

        // CHECK EXPIRY STATUS
        if (expiryAlertsEnabled) {

            String expiryStatus =
                    getExpiryStatus(
                            pantryItem.getExpiryDate()
                    );

            if (!expiryStatus.isEmpty()) {

                status.append(
                        expiryStatus
                );
            }
        }

        // CHECK LOW STOCK STATUS
        if (pantryItem.getQuantity()
                <= pantryItem.getLowStockThreshold()) {

            if (status.length() > 0) {

                status.append(
                        " • "
                );
            }

            status.append(
                    "Low Stock"
            );
        }

        // SHOW OR HIDE STATUS
        if (status.length() == 0) {

            holder.textPantryStatus.setVisibility(
                    View.GONE
            );

        } else {

            holder.textPantryStatus.setVisibility(
                    View.VISIBLE
            );

            holder.textPantryStatus.setText(
                    status.toString()
            );
        }

        // SELECT INGREDIENT FOR EDITING/DELETING
        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {

                listener.onItemClick(
                        pantryItem
                );
            }
        });
    }

    @Override
    public int getItemCount() {

        return pantryItems.size();
    }

    // CHECK WHETHER AN INGREDIENT IS EXPIRED
    // OR WILL EXPIRE WITHIN 7 DAYS
    private String getExpiryStatus(
            String expiryDateText) {

        if (expiryDateText == null
                || expiryDateText.trim().isEmpty()) {

            return "";
        }

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        sdf.setLenient(false);

        try {

            Date expiryDate =
                    sdf.parse(
                            expiryDateText
                    );

            if (expiryDate == null) {

                return "";
            }

            Calendar todayCalendar =
                    Calendar.getInstance();

            todayCalendar.set(
                    Calendar.HOUR_OF_DAY,
                    0
            );

            todayCalendar.set(
                    Calendar.MINUTE,
                    0
            );

            todayCalendar.set(
                    Calendar.SECOND,
                    0
            );

            todayCalendar.set(
                    Calendar.MILLISECOND,
                    0
            );

            Date today =
                    todayCalendar.getTime();

            long difference =
                    expiryDate.getTime()
                            - today.getTime();

            long daysRemaining =
                    TimeUnit.MILLISECONDS.toDays(
                            difference
                    );

            if (daysRemaining < 0) {

                return "Expired";

            } else if (daysRemaining <= 7) {

                return "Expiring Soon";
            }

        } catch (ParseException e) {

            return "";
        }

        return "";
    }

    public static class PantryViewHolder
            extends RecyclerView.ViewHolder {

        TextView textPantryName;
        TextView textPantryDetails;
        TextView textPantryStatus;

        public PantryViewHolder(
                @NonNull View itemView) {

            super(itemView);

            textPantryName =
                    itemView.findViewById(
                            R.id.textPantryName
                    );

            textPantryDetails =
                    itemView.findViewById(
                            R.id.textPantryDetails
                    );

            textPantryStatus =
                    itemView.findViewById(
                            R.id.textPantryStatus
                    );
        }
    }
}