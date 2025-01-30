package com.example.car_main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.HashMap;
import java.util.Map;

public class EnterCodeActivity extends AppCompatActivity {

    EditText codeInput;
    Button verifyButton;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        codeInput = findViewById(R.id.codeInput);
        verifyButton = findViewById(R.id.verifyCodeButton);

        email = getIntent().getStringExtra("email");

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });
    }

    private void verifyCode() {
        final String code = codeInput.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "Please enter the reset code", Toast.LENGTH_SHORT).show();
            return;
        }

        String updateUrl = Constants.BASE_URL + "verify_code.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().contains("success")) {
                            Intent intent = new Intent(EnterCodeActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("reset_code", code); // Pass the reset code to the next activity
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EnterCodeActivity.this, "Invalid or expired code", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EnterCodeActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("reset_code", code);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
