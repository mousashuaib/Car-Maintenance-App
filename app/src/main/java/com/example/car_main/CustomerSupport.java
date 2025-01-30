package com.example.car_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CustomerSupport extends AppCompatActivity {

    private EditText nameEditText, emailEditText, subjectEditText, messageEditText, phoneEditText;
    private Button submitButton;
    private TextView chatLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        subjectEditText = findViewById(R.id.subjectEditText);
        messageEditText = findViewById(R.id.messageEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        submitButton = findViewById(R.id.submitButton);
        chatLink = findViewById(R.id.chatLink);

        // Load data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("FIRST_NAME", "") + " " + sharedPreferences.getString("LAST_NAME", "");
        String email = sharedPreferences.getString("EMAIL", "");
        String phone = sharedPreferences.getString("MOBILE", "");

        nameEditText.setText(name);
        emailEditText.setText(email);
        phoneEditText.setText(phone);

        // Handle submit button click
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredName = nameEditText.getText().toString().trim();
                String enteredEmail = emailEditText.getText().toString().trim();
                String enteredPhone = phoneEditText.getText().toString().trim();
                String enteredSubject = subjectEditText.getText().toString().trim();
                String enteredMessage = messageEditText.getText().toString().trim();

                if (enteredName.isEmpty() || enteredEmail.isEmpty() || enteredPhone.isEmpty() || enteredSubject.isEmpty() || enteredMessage.isEmpty()) {
                    Toast.makeText(CustomerSupport.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendMessageToAdmin(enteredName, enteredEmail, enteredPhone, enteredSubject, enteredMessage);
            }
        });

        // Handle chat link click
        chatLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerSupport.this, LiveChat.class);
                startActivity(intent);
            }
        });
    }

    private void sendMessageToAdmin(String name, String email, String phone, String subject, String message) {
        String url = Constants.BASE_URL + "sendMessage.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CustomerSupport.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                        subjectEditText.setText("");
                        messageEditText.setText("");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomerSupport.this, "Error sending message", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("subject", subject);
                params.put("message", message);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
