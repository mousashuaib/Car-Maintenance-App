package com.example.car_main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class AdminHomeActivity extends AppCompatActivity {
    private DrawerLayout admin_drawer;
    private NavigationView navigationView;
    private Button btn1 , btn5,btn3,btn4 ,btn12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        btn1 = findViewById(R.id.btn1);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn12 = findViewById(R.id.btn12);


        admin_drawer = findViewById(R.id.admin_drawer);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, admin_drawer, R.string.openDrawer, R.string.closeDrawer);
        admin_drawer.addDrawerListener(toggle);
        toggle.syncState();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchPage = new Intent(AdminHomeActivity.this , AdminOptionsActivity.class);
                startActivity(searchPage);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoritePage = new Intent(AdminHomeActivity.this, ManageUsersActivity.class);
                startActivity(favoritePage);
            }
        });

        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoritePage = new Intent(AdminHomeActivity.this, ShowCars.class);
                startActivity(favoritePage);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoritePage = new Intent(AdminHomeActivity.this, CustomerSupportAdminActivity.class);
                startActivity(favoritePage);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoritePage = new Intent(AdminHomeActivity.this, AdminChatUsersActivity.class);
                startActivity(favoritePage);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;

            if (item.getItemId() == R.id.ProfileManager) {
                intent = new Intent(this, ViewProfile.class);
                intent.putExtra("showOnlyAddAndManage", true); // Pass extra to show only certain buttons
                startActivity(intent);
            }
            admin_drawer.closeDrawer(GravityCompat.START);
            return true;
        });


    }
}