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

public class ViewAllBookingsActivity extends AppCompatActivity {

    private RecyclerView rvBookings;
    private BookingsAdapter bookingsAdapter;
    private List<Booking> bookingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        rvBookings = findViewById(R.id.rvBookings);
        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        bookingsAdapter = new BookingsAdapter(bookingList);
        rvBookings.setAdapter(bookingsAdapter);

        fetchAllBookings();
    }

    private void fetchAllBookings() {
        String url = Constants.BASE_URL + "fetch_all_bookings.php";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONArray bookingsArray = jsonResponse.getJSONArray("bookings");

                            for (int i = 0; i < bookingsArray.length(); i++) {
                                JSONObject booking = bookingsArray.getJSONObject(i);
                                String partName = booking.getString("part_name");
                                String repairDate = booking.getString("repair_date");
                                bookingList.add(new Booking(partName, repairDate));
                            }
                            bookingsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No bookings found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response Parsing Error!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
