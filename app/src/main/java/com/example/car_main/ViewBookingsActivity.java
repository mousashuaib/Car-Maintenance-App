package com.example.car_main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewBookingsActivity extends AppCompatActivity {

    private RecyclerView rvBookings;
    private TextView tvNoBookings;
    private BookingsAdapter bookingsAdapter;
    private List<Booking> bookingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        rvBookings = findViewById(R.id.rvBookings);
        tvNoBookings = findViewById(R.id.tvNoBookings);

        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        bookingsAdapter = new BookingsAdapter(bookingList);
        rvBookings.setAdapter(bookingsAdapter);

        fetchBookings();
    }

    private void fetchBookings() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("ID", null);

        if (userId == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String url = Constants.BASE_URL + "fetch_user_bookings.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONArray bookingsArray = jsonResponse.getJSONArray("bookings");

                            if (bookingsArray.length() == 0) {
                                tvNoBookings.setVisibility(View.VISIBLE);
                            } else {
                                tvNoBookings.setVisibility(View.GONE);
                                for (int i = 0; i < bookingsArray.length(); i++) {
                                    JSONObject booking = bookingsArray.getJSONObject(i);
                                    String partName = booking.getString("part_name");
                                    String repairDate = booking.getString("repair_date");
                                    bookingList.add(new Booking(partName, repairDate));
                                }
                                bookingsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
