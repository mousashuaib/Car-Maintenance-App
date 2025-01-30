package com.example.car_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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

public class EditProfile extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtEmail;
    private EditText edtPhone;
    private EditText edtFName;
    private EditText edtLName;
    private Button btn_save;
    private String oldname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtFName = findViewById(R.id.edtFName);
        edtLName = findViewById(R.id.edtLName);
        btn_save = findViewById(R.id.btn_save);

        // Load data from SharedPreferences
        loadDataFromPreferences();

        // Save button click listener
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String mobile = edtPhone.getText().toString().trim();
                String firstName = edtFName.getText().toString().trim();
                String lastName = edtLName.getText().toString().trim();

                if (!validateInput(username, email, mobile)) return;

                // Confirm changes with password input
                promptPasswordConfirmation(username, firstName, lastName, email, mobile);
            }
        });

        // Handle back press with confirmation
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(EditProfile.this)
                        .setTitle("Discard Changes?")
                        .setMessage("Do you want to discard the changes or keep editing?")
                        .setPositiveButton("Discard", (dialog, which) -> finish())
                        .setNegativeButton("Keep Editing", null)
                        .show();
            }
        });
    }

    private void loadDataFromPreferences() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        oldname = sharedPreferences.getString("USERNAME", ""); // Retrieve the old username
        edtUsername.setText(sharedPreferences.getString("USERNAME", ""));
        edtEmail.setText(sharedPreferences.getString("EMAIL", ""));
        edtPhone.setText(sharedPreferences.getString("MOBILE", ""));
        edtFName.setText(sharedPreferences.getString("FIRST_NAME", ""));
        edtLName.setText(sharedPreferences.getString("LAST_NAME", ""));


    }

    private boolean validateInput(String username, String email, String mobile) {
        if (username.isEmpty() || email.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mobile.length() < 10) {
            Toast.makeText(this, "Phone number must be at least 10 digits!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void promptPasswordConfirmation(String username, String firstName, String lastName, String email, String mobile) {
        final EditText inputPassword = new EditText(this);
        inputPassword.setHint("Enter your password");
        inputPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(this)
                .setTitle("Confirm Changes")
                .setMessage("Are these details correct?\n\n"
                        + "Name: " + username + "\n"
                        + "First Name: " + firstName + "\n"
                        + "Last Name: " + lastName + "\n"
                        + "Email: " + email + "\n"
                        + "Mobile: " + mobile)
                .setView(inputPassword)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String password = inputPassword.getText().toString().trim();
                    if (password.isEmpty()) {
                        Toast.makeText(EditProfile.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        validateAndSaveChanges(username, firstName, lastName, email, mobile,password);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void validateAndSaveChanges(String username, String firstName, String lastName, String email, String mobile, String password) {
        String url = Constants.BASE_URL + "validatePassword.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if (status.equals("success")) {


                                updateUserDetails(username, firstName, lastName, email, mobile);
                            } else {
                                Toast.makeText(EditProfile.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProfile.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String username = sharedPreferences.getString("USERNAME", "");
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void updateUserDetails(String username, String firstName, String lastName, String email, String mobile) {
        String url = Constants.BASE_URL + "updateUser.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if (status.equals("success")) {
                                saveChanges(username, firstName, lastName, email, mobile);
                            } else {
                                Toast.makeText(EditProfile.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProfile.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("oldname", oldname); // Pass the old username to the server
                params.put("username", username); // New or unchanged username
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("email", email);
                params.put("mobile", mobile);
                return params;
            }

        };

        queue.add(stringRequest);
    }

    private void saveChanges(String username, String firstName, String lastName, String email, String mobile) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("USERNAME", username);
        editor.putString("EMAIL", email);
        editor.putString("MOBILE", mobile);
        editor.putString("FIRST_NAME", firstName);
        editor.putString("LAST_NAME", lastName);
        editor.apply();

        Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(EditProfile.this, UserHomeActivity.class);
        startActivity(intent);
    }
}
