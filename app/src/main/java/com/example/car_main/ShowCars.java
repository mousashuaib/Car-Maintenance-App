package com.example.car_main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowCars extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private List<Car> carList;
    private static final String URL = Constants.BASE_URL + "getCars.php"; // Using Constants.BASE_URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cars);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Initialize the adapter with an empty list
        carList = new ArrayList<>();
        carAdapter = new CarAdapter(ShowCars.this, carList);
        recyclerView.setAdapter(carAdapter); // Attach adapter immediately

        fetchCars(); // Fetch cars and update the adapter later
    }

    private void fetchCars() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {
                                JSONArray cars = jsonResponse.getJSONArray("data");
                                for (int i = 0; i < cars.length(); i++) {
                                    JSONObject carObject = cars.getJSONObject(i);
                                    Car car = new Car(
                                            carObject.getString("id"),
                                            carObject.getString("car_name"),
                                            carObject.getString("car_model"),
                                            carObject.getString("license_plate"),
                                            carObject.getString("car_image")
                                    );
                                    carList.add(car);
                                }
                                carAdapter.notifyDataSetChanged(); // Notify changes to adapter
                            } else {
                                Toast.makeText(ShowCars.this, "No cars found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ShowCars.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShowCars.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            String carId = data.getStringExtra("car_id");
            String carName = data.getStringExtra("car_name");
            String carModel = data.getStringExtra("car_model");
            String licensePlate = data.getStringExtra("license_plate");
            String carImage = data.getStringExtra("car_image");

            for (int i = 0; i < carList.size(); i++) {
                if (carList.get(i).getId().equals(carId)) {
                    // Update the car details
                    carList.get(i).setCarName(carName);
                    carList.get(i).setCarModel(carModel);
                    carList.get(i).setLicensePlate(licensePlate);
                    carList.get(i).setCarImage(carImage);

                    // Refresh the specific item in the RecyclerView
                    carAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }



}
