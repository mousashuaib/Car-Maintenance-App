package com.example.car_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PeriodicMaintenanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodic_maintenance);

        OrdersManager ordersManager = OrdersManager.getInstance();

        Button btOil = findViewById(R.id.btOil);
        btOil.setOnClickListener(v -> {
            ordersManager.addOrder(new Service("Oil Change", R.drawable.oil, "$50"));
            saveOrderToDatabase("Oil Change", "$50");
            Toast.makeText(this, "Added Oil Change to orders", Toast.LENGTH_SHORT).show();
        });

        Button btBattery = findViewById(R.id.btBattery);
        btBattery.setOnClickListener(v -> {
            ordersManager.addOrder(new Service("Battery Check", R.drawable.battery, "$30"));
            saveOrderToDatabase("Battery Check", "$30");
            Toast.makeText(this, "Added Battery Check to orders", Toast.LENGTH_SHORT).show();
        });

        Button btFilter = findViewById(R.id.btFilter);
        btFilter.setOnClickListener(v -> {
            ordersManager.addOrder(new Service("Filter Change", R.drawable.filter, "$40"));
            saveOrderToDatabase("Filter Change", "$40");
            Toast.makeText(this, "Added Filter Change to orders", Toast.LENGTH_SHORT).show();
        });

        Button btBrake = findViewById(R.id.btBrake);
        btBrake.setOnClickListener(v -> {
            ordersManager.addOrder(new Service("Brakes Check", R.drawable.brake, "$60"));
            saveOrderToDatabase("Brakes Check", "$60");
            Toast.makeText(this, "Added Brakes Check to orders", Toast.LENGTH_SHORT).show();
        });


        Button btOrders = findViewById(R.id.btPay);
        btOrders.setOnClickListener(v -> {
            Intent intent = new Intent(PeriodicMaintenanceActivity.this, OrdersActivity.class);
            startActivity(intent);
        });
    }
    private void saveOrderToDatabase(String serviceName, String servicePrice) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("ID", null); // Retrieve user ID from SharedPreferences

        if (userId == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Constants.BASE_URL + "save_order.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            Toast.makeText(this, "Order saved successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response Parsing Error!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("service_name", serviceName);
                params.put("service_price", servicePrice);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

}
