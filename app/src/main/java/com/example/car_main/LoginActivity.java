package com.example.car_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUsername, loginPassword;
    private Button loginButton, registerButton, forgetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.username);
        loginPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.signUp);
        forgetPasswordButton = findViewById(R.id.forgetPassword);

        // Handle login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        // Navigate to Register screen
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, register.class);
                startActivity(intent);
            }
        });

        // Navigate to Forgot Password screen
        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin() {
        final String username = loginUsername.getText().toString().trim();
        final String password = loginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String updateUrl = Constants.BASE_URL + "login.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("ServerResponse", response); // Log server response for debugging
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if (status.equals("success")) {
                                // Extract user details
                                String role = jsonResponse.getString("user_type");
                                String email = jsonResponse.optString("email", "N/A");
                                String mobile = jsonResponse.optString("mobile", "N/A");
                                String firstName = jsonResponse.optString("first_name", "N/A");
                                String lastName = jsonResponse.optString("last_name", "N/A");
                                String id = jsonResponse.optString("id", ""); // Ensure "id" is included

                                if (id.isEmpty()) {
                                    Toast.makeText(LoginActivity.this, "Server response missing user ID", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Save data in SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("USERNAME", username);
                                editor.putString("EMAIL", email);
                                editor.putString("MOBILE", mobile);
                                editor.putString("FIRST_NAME", firstName);
                                editor.putString("LAST_NAME", lastName);
                                editor.putString("ROLE", role);
                                editor.putString("ID", id);
                                editor.apply();

                                // Navigate based on role
                                if (role.equals("admin")) {
                                    startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                                } else {
                                    startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                                }
                                finish();
                            } else {
                                String message = jsonResponse.getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMsg = (error.getMessage() != null) ? error.getMessage() : "Unable to connect to server";
                Toast.makeText(LoginActivity.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                Log.e("VolleyError", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
