package com.example.car_main;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerSupportAdminActivity extends AppCompatActivity {

    private ListView chatListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support_admin);

        // Initialize ListView and ArrayList
        chatListView = findViewById(R.id.chatListView);
        userMessages = new ArrayList<>();

        // Initialize ArrayAdapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userMessages);
        chatListView.setAdapter(adapter);

        // Fetch chat messages from the server
        fetchChatMessages();
    }

    private void fetchChatMessages() {
        String url = Constants.BASE_URL + "getMessages.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if (status.equals("success")) {
                                // Get the "messages" array
                                JSONArray messagesArray = jsonResponse.getJSONArray("messages");

                                // Loop through each message
                                for (int i = 0; i < messagesArray.length(); i++) {
                                    JSONObject messageObject = messagesArray.getJSONObject(i);

                                    String name = messageObject.getString("name");
                                    String email = messageObject.getString("email");
                                    String phone = messageObject.getString("phone");
                                    String subject = messageObject.getString("subject");
                                    String message = messageObject.getString("message");
                                    String createdAt = messageObject.getString("created_at");

                                    // Add to the list
                                    userMessages.add("Name: " + name + "\n" +
                                            "Email: " + email + "\n" +
                                            "Phone: " + phone + "\n" +
                                            "Subject: " + subject + "\n" +
                                            "Message: " + message + "\n" +
                                            "Date: " + createdAt);
                                }

                                // Notify adapter about data changes
                                adapter.notifyDataSetChanged();
                            } else {
                                String errorMessage = jsonResponse.getString("message");
                                Toast.makeText(CustomerSupportAdminActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CustomerSupportAdminActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomerSupportAdminActivity.this, "Error fetching messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}
