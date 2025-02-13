package com.example.car_main;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveChat extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText chatInput;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private List<String> chatMessages;
    private RequestQueue requestQueue;
    private String userId; // Fetch this from SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatInput = findViewById(R.id.chatInput);
        sendButton = findViewById(R.id.sendButton);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        requestQueue = Volley.newRequestQueue(this);

        // Fetch user ID from SharedPreferences
        userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("ID", "");

        // Periodically fetch messages
        fetchMessages();
        startMessagePolling();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatInput.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    sendMessage(message);
                    chatInput.setText("");
                } else {
                    Toast.makeText(LiveChat.this, "Please type a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchMessages() {
        String url = Constants.BASE_URL + "getChatMessages.php?user_id=" + userId;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                JSONArray messages = response.getJSONArray("messages");
                                chatMessages.clear();
                                for (int i = 0; i < messages.length(); i++) {
                                    JSONObject message = messages.getJSONObject(i);
                                    String senderType = message.getString("sender_type");
                                    String text = message.getString("message");
                                    chatMessages.add(senderType + ": " + text);
                                }
                                chatAdapter.notifyDataSetChanged();
                                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LiveChat.this, "Error fetching messages", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    private void sendMessage(String message) {
        String url = Constants.BASE_URL + "sendChatMessage.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fetchMessages();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LiveChat.this, "Error sending message", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("sender_type", "user");
                params.put("message", message);
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void startMessagePolling() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                fetchMessages();
                handler.postDelayed(this, 3000); // Fetch messages every 3 seconds
            }
        };
        handler.post(runnable);
    }
}
