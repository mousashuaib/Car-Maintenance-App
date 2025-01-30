package com.example.car_main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminOptionsActivity extends AppCompatActivity {

    private Button btnUsersBookedDate, btnUsersOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);

        btnUsersBookedDate = findViewById(R.id.btnUsersBookedDate);
        btnUsersOrders = findViewById(R.id.btnUsersOrders);

        // Navigate to Users Booked Date
        btnUsersBookedDate.setOnClickListener(v -> {
            Intent intent = new Intent(AdminOptionsActivity.this, ViewAllBookingsActivity.class); // Create this activity for showing bookings
            startActivity(intent);
        });

        // Navigate to Users Orders (Placeholder for now)
        btnUsersOrders.setOnClickListener(v -> {
            Intent intent = new Intent(AdminOptionsActivity.this, UsersOrdersActivity.class); // To be implemented later
            startActivity(intent);
        });
    }
}
