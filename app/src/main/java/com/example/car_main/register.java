package com.example.car_main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    TextInputEditText regUsername, regPassword, regMobile, regFirstName, regLastName, regEmail, regConfirmEmail;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUsername = findViewById(R.id.reg_username);
        regPassword = findViewById(R.id.reg_password);
        regMobile = findViewById(R.id.reg_mobile);
        regFirstName = findViewById(R.id.reg_firstName);
        regLastName = findViewById(R.id.reg_lastName);
        regEmail = findViewById(R.id.reg_email);
        regConfirmEmail = findViewById(R.id.reg_confirmemail);
        registerButton = findViewById(R.id.reg_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });
    }

    private void performRegistration() {
        String username = regUsername.getText().toString().trim();
        String password = regPassword.getText().toString().trim();
        String mobile = regMobile.getText().toString().trim();
        String firstName = regFirstName.getText().toString().trim();
        String lastName = regLastName.getText().toString().trim();
        String email = regEmail.getText().toString().trim();
        String confirmEmail = regConfirmEmail.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(mobile) ||
                TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(confirmEmail)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.equals(confirmEmail)) {
            Toast.makeText(this, "Emails do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Constants.BASE_URL + "register.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase("success")) {
                            Toast.makeText(register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(register.this,LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(register.this, "Registration failed: " + response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(register.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("mobile", mobile);
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("email", email);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
