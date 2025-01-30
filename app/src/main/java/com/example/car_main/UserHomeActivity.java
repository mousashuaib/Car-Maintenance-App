package com.example.car_main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class UserHomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar); // Set the Toolbar as the app's ActionBar

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            drawerLayout = findViewById(R.id.drawer_layout2);
            navigationView = findViewById(R.id.nav_view2);

            btn1 = findViewById(R.id.btn1);
            btn2 = findViewById(R.id.btn2);
            btn3 = findViewById(R.id.btn3);
            btn4 = findViewById(R.id.btn4);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, R.string.openDrawer, R.string.closeDrawer);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Handle Navigation Item Clicks
            navigationView.setNavigationItemSelectedListener(item -> {
                if (item.getItemId() == R.id.nav_notifications) {
                    startActivity(new Intent(this, NotificationsActivity.class));
                } else if (item.getItemId() == R.id.nav_offers) {
                    startActivity(new Intent(this, OffersActivity.class));
                } else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(this, ViewProfile.class));

                } else if (item.getItemId() == R.id.nav_customer_support) {
                    startActivity(new Intent(this, CustomerSupport.class));
                }

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            });
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    new AlertDialog.Builder(UserHomeActivity.this)
                            .setTitle("Are you sure you want to exit?")
                            .setMessage("Do you want to Exit the car application?")
                            .setPositiveButton("Exit", (dialog, which) -> finish())
                            .setNegativeButton("Stay", null)
                            .show();
                }
            });
        }

            // Button Click Listeners
            btn1.setOnClickListener(v -> startActivity(new Intent(UserHomeActivity.this, ShowCars.class)));
            btn3.setOnClickListener(v -> startActivity(new Intent(UserHomeActivity.this, HabeebActivity.class)));
            btn4.setOnClickListener(v -> startActivity(new Intent(UserHomeActivity.this, AddCar.class)));
        }


    }

