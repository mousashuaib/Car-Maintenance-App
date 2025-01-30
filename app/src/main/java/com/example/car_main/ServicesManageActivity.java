package com.example.car_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ServicesManageActivity extends AppCompatActivity {
    private Button btn1, btn2, btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_manage);

        setUpViews();
    }
    private void setUpViews(){
        btn1= findViewById(R.id.button3);
        btn2= findViewById(R.id.button4);
        btn3= findViewById(R.id.button5);
    }
    public void onClickFabBackMyCustomers(View v) { // Navigate back to the MainActivity when the floating action button is clicked
        Intent intent = new Intent(ServicesManageActivity.this, UserHomeActivity.class);
        startActivity(intent);
    }
}