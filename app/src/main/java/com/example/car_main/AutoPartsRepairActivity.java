package com.example.car_main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class AutoPartsRepairActivity extends AppCompatActivity {

    private int userId; // To store the fetched user ID
    Button btBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_parts_repair);

        // Retrieve user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("ID", null); // Retrieve as a String

        if (userIdString == null) {
            Toast.makeText(this, "User not logged in! Please log in first.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if user ID is missing
            return;
        }

        int userId = -1;
        try {
            userId = Integer.parseInt(userIdString); // Convert to integer if needed
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid user ID. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Proceed with the rest of your activity logic
        Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_SHORT).show();

        // Example setup for parts and booking
        Part engine = new Part("Engine", R.drawable.engine, "Converts fuel into mechanical power");
        Part exhaust = new Part("Exhaust", R.drawable.exhu, "Removes engine waste gases");
        Part clutch = new Part("Clutch", R.drawable.clutch, "Engages and disengages power transmission");
        Part suspension = new Part("Suspension", R.drawable.sus, "Absorbs shocks for smooth rides");

        // Define buttons
        Button btEngine = findViewById(R.id.btEngine);
        Button btExhu = findViewById(R.id.btExhu);
        Button btClutch = findViewById(R.id.btClutch);
        Button btSuspension = findViewById(R.id.btSus);
        Button btBooks=findViewById(R.id.btBooks);

        // Set booking handlers for each part
        setBookingHandler(btEngine, engine, userId);
        setBookingHandler(btExhu, exhaust, userId);
        setBookingHandler(btClutch, clutch, userId);
        setBookingHandler(btSuspension, suspension, userId);
        btBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutoPartsRepairActivity.this, ViewBookingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setBookingHandler(Button button, Part part, int userId) {
        button.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String repairDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        saveBookingToDatabase(part.getName(), repairDate, userId);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void saveBookingToDatabase(String partName, String repairDate, int userId) {
        String url = Constants.BASE_URL + "save_booking.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        } else {
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
                params.put("part_name", partName);
                params.put("repair_date", repairDate);
                params.put("user_id", String.valueOf(userId)); // Pass the user ID
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

}
