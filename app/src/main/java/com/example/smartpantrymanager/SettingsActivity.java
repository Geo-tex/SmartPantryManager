package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryAlerts;

    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME =
            "SmartPantrySettings";

    private static final String KEY_EXPIRY_ALERTS =
            "expiryAlerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        switchExpiryAlerts =
                findViewById(
                        R.id.switchExpiryAlerts
                );

        Button buttonSaveSettings =
                findViewById(
                        R.id.buttonSaveSettings
                );

        sharedPreferences =
                getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                );

        loadSettings();

        buttonSaveSettings.setOnClickListener(v ->
                saveSettings()
        );
    }

    private void loadSettings() {

        boolean expiryAlertsEnabled =
                sharedPreferences.getBoolean(
                        KEY_EXPIRY_ALERTS,
                        true
                );

        switchExpiryAlerts.setChecked(
                expiryAlertsEnabled
        );
    }

    private void saveSettings() {

        boolean expiryAlertsEnabled =
                switchExpiryAlerts.isChecked();

        SharedPreferences.Editor editor =
                sharedPreferences.edit();

        editor.putBoolean(
                KEY_EXPIRY_ALERTS,
                expiryAlertsEnabled
        );

        editor.apply();

        Toast.makeText(
                this,
                "Settings saved successfully",
                Toast.LENGTH_SHORT
        ).show();
    }
}