package com.example.car_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentMethod extends AppCompatActivity {

    private EditText etLicensePlate, etAmount, etCardNumber;
    private Spinner spinnerPaymentMethod;
    private Button btnSubmitPayment;
    double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        // Initialize the views
        etLicensePlate = findViewById(R.id.etLicensePlate);
        etAmount = findViewById(R.id.etAmount); // Ensure this is initialized before use
        etCardNumber = findViewById(R.id.etCardNumber);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);
        btnSubmitPayment = findViewById(R.id.btnSubmitPayment);

        // Retrieve the price passed from OrdersActivity
        String amounts = getIntent().getStringExtra("amount");
        if (amounts != null) {
            etAmount.setText(amounts); // Set the price to etAmount
        }

        // Set up Spinner Adapter
        ArrayAdapter<String> paymentMethodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Cash", "Card"});
        paymentMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(paymentMethodAdapter);

        // Spinner selection listener
        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 1) { // "Card" is selected
                    etCardNumber.setVisibility(View.VISIBLE); // Show card number field
                } else {
                    etCardNumber.setVisibility(View.GONE); // Hide card number field
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });

        // Submit Button Click Listener
        btnSubmitPayment.setOnClickListener(v -> {
            String licensePlate = etLicensePlate.getText().toString();
            String amount = etAmount.getText().toString();
            String paymentMethod = spinnerPaymentMethod.getSelectedItem().toString();
            String cardNumber = etCardNumber.getText().toString(); // Only used if "Card" is selected

            // Validate inputs
            if (licensePlate.isEmpty() || amount.isEmpty() || paymentMethod.isEmpty()) {
                Toast.makeText(PaymentMethod.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Ensure card number is entered if payment method is "Card"
                if (paymentMethod.equals("Card") && cardNumber.isEmpty()) {
                    Toast.makeText(PaymentMethod.this, "Card number is required when payment method is Card", Toast.LENGTH_SHORT).show();
                } else {
                    // Call the function to submit the payment data
                    submitPayment(licensePlate, paymentMethod, amount, cardNumber);
                }
            }
        });
    }


    // Submit payment method to the backend using Volley
    private void submitPayment(String licensePlate, String paymentMethod, String amount, String cardNumber) {
        String url = Constants.BASE_URL + "PaymentMethod.php";

        // Create the request JSON object
        JSONObject paymentData = new JSONObject();
        try {
            paymentData.put("license_plate", licensePlate);
            paymentData.put("payment_method", paymentMethod);
            paymentData.put("amount", amount);
            if ("Card".equals(paymentMethod)) {
                paymentData.put("card_number", cardNumber); // Only include card number if payment method is "Card"
            } else {
                paymentData.put("card_number", JSONObject.NULL); // Null if not used
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }





        // Create the Volley request to send data
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                Toast.makeText(PaymentMethod.this, "Payment successfully recorded", Toast.LENGTH_SHORT).show();
                                resetForm(); // Reset the form after successful payment
                            } else {
                                Toast.makeText(PaymentMethod.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PaymentMethod.this, "Response Parsing Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PaymentMethod.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public byte[] getBody() {
                return paymentData.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void resetForm() {
        // Clear all input fields
        etLicensePlate.setText("");
        etAmount.setText("");
        etCardNumber.setText("");

        // Reset the payment method spinner to the default value
        spinnerPaymentMethod.setSelection(0);

        // Hide the card number field
        etCardNumber.setVisibility(View.GONE);
    }

}
