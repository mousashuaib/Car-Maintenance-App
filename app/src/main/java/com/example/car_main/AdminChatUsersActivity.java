package com.example.car_main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminChatUsersActivity extends AppCompatActivity {

    private ListView usersListView;
    private ArrayList<String> usersList;
    private ArrayAdapter<String> adapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat_users);

        usersListView = findViewById(R.id.usersListView);
        usersList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        usersListView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        fetchChatUsers();

        usersListView.setOnItemClickListener((parent, view, position, id) -> {
            String userId = (String) parent.getItemAtPosition(position);
            Intent intent = new Intent(AdminChatUsersActivity.this, AdminChatActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }

    private void fetchChatUsers() {
        String url = Constants.BASE_URL + "fetchChatUsers.php";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                JSONArray users = response.getJSONArray("users");
                                usersList.clear();
                                for (int i = 0; i < users.length(); i++) {
                                    JSONObject user = users.getJSONObject(i);
                                    String userInfo = user.getString("id") + " - " + user.getString("username");
                                    usersList.add(userInfo);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AdminChatUsersActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminChatUsersActivity.this, "Error fetching users", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }
}
