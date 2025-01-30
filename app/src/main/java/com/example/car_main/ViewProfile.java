package com.example.car_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewProfile extends AppCompatActivity {

    private TextView txtName;
    private TextView txtEmail;
    private TextView txtPhone;
    private TextView txtFName;
    private TextView txtLName;
    private Button btnEdit;
    private Button btnLogOut;
    private Button btnPassChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtFName = findViewById(R.id.txtfName);
        txtLName = findViewById(R.id.txtlName);
        btnEdit = findViewById(R.id.btn_edit);
        btnPassChange = findViewById(R.id.btn_pass_change);
        btnLogOut = findViewById(R.id.btn_logOut);

        // Retrieve data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME", "N/A");
        String email = sharedPreferences.getString("EMAIL", "N/A");
        String phone = sharedPreferences.getString("MOBILE", "N/A");
        String firstName = sharedPreferences.getString("FIRST_NAME", "N/A");
        String lastName = sharedPreferences.getString("LAST_NAME", "N/A");

        // Display user information
        txtName.setText(username);
        txtEmail.setText(email);
        txtPhone.setText(phone);
        txtFName.setText(firstName);
        txtLName.setText(lastName);

        // Edit Profile Button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, EditProfile.class);
                startActivity(intent);
            }
        });

        // Change Password Button
        btnPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, passChangeActivity.class);
                startActivity(intent);
            }
        });

        // Log Out Button
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Navigate back to LoginActivity
                Toast.makeText(ViewProfile.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewProfile.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}