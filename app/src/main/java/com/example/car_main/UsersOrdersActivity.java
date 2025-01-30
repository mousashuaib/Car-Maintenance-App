package com.example.car_main;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class UsersOrdersActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private OrdersAdapter ordersAdapter;
    private List<Order> orderList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_orders);

        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        ordersAdapter = new OrdersAdapter(orderList);
        rvOrders.setAdapter(ordersAdapter);

        fetchAllOrders();
    }

    private void fetchAllOrders() {
        String url = Constants.BASE_URL + "fetch_all_orders.php";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Log the server response for debugging
                    android.util.Log.d("ServerResponse", response);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONArray ordersArray = jsonResponse.getJSONArray("orders");

                            for (int i = 0; i < ordersArray.length(); i++) {
                                JSONObject order = ordersArray.getJSONObject(i);
                                String userName = order.getString("user_name");
                                String serviceName = order.getString("service_name");
                                String servicePrice = order.getString("service_price");
                                String createdAt = order.getString("created_at");

                                orderList.add(new Order(userName, serviceName, servicePrice, createdAt));
                            }
                            ordersAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response Parsing Error!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Log the network error
                    android.util.Log.e("VolleyError", error.toString());
                    Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }

}
