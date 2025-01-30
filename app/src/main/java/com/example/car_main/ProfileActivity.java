package com.example.car_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setUpViews();
    }
    private void setUpViews(){
        btn1= findViewById(R.id.button);
        btn2= findViewById(R.id.button2);
    }
    public void onClickFabBackMyCustomers(View v) { // Navigate back to the MainActivity when the floating action button is clicked
        Intent intent = new Intent(ProfileActivity.this, UserHomeActivity.class);
        startActivity(intent);
    }
    public void onClickCarManage(View v) { // Navigate back to the MainActivity when the floating action button is clicked
        Intent intent = new Intent(ProfileActivity.this, ShowCars.class);
        startActivity(intent);
    }
    public void onClickFeedBack(View v) { // Navigate back to the MainActivity when the floating action button is clicked
        Intent intent = new Intent(ProfileActivity.this, FeedbackActivity.class);
        startActivity(intent);
    }
}