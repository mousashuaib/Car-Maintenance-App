package com.example.car_main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HabeebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habeeb); // Update the layout file name accordingly

        // Navigate to Auto Parts Repair Activity
        Button btAuto = findViewById(R.id.btAuto);
        btAuto.setOnClickListener(v -> {
            Intent intent = new Intent(HabeebActivity.this, AutoPartsRepairActivity.class);
            startActivity(intent);
        });

        // Navigate to Periodic Maintenance Activity
        Button btMaint = findViewById(R.id.btMaint);
        btMaint.setOnClickListener(v -> {
            Intent intent = new Intent(HabeebActivity.this, PeriodicMaintenanceActivity.class);
            startActivity(intent);
        });
    }
}
