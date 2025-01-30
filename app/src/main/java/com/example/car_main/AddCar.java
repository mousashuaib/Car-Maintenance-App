package com.example.car_main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddCar extends AppCompatActivity {

    private EditText etCarName, etCarModel, licensePlateEditText;
    private Button btnSelectImage, btnSave;
    private ImageView imgCarImage;
    private Uri carImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        // Initialize UI elements
        etCarName = findViewById(R.id.carNameEditText);
        etCarModel = findViewById(R.id.carModelEditText);
        licensePlateEditText = findViewById(R.id.licensePlateEditText);
        btnSelectImage = findViewById(R.id.selectImageButton);
        btnSave = findViewById(R.id.saveButton);
        imgCarImage = findViewById(R.id.carImageView);

        // On Click Listener to select image
        btnSelectImage.setOnClickListener(v -> openFileChooser());

        // On Click Listener to save the car details
        btnSave.setOnClickListener(v -> saveCarDetails());
    }

    // Open file chooser to select car image
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  // Intent to pick a file
        intent.setType("image/*");  // Filter for images
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If image is selected
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            carImageUri = data.getData();
            imgCarImage.setImageURI(carImageUri); // Display selected image in ImageView
        }
    }

    // Save car details to database
    private void saveCarDetails() {
        String carName = etCarName.getText().toString();
        String carModel = etCarModel.getText().toString();
        String licensePlate = licensePlateEditText.getText().toString();

        // Validate inputs
        if (carName.isEmpty() || carModel.isEmpty() || licensePlate.isEmpty() || carImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
        } else {
            // Call the function to upload data and image
            uploadCarData(carName, carModel, licensePlate, carImageUri);
        }
    }

    // Upload car data and image to server
    private void uploadCarData(String carName, String carModel, String licensePlate, Uri carImageUri) {
        String url = Constants.BASE_URL + "Addcar.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "Response: " + response); // Log the response
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            // If success, show a message and reset fields
                            if (success) {
                                Toast.makeText(AddCar.this, "Car added successfully", Toast.LENGTH_SHORT).show();
                                clearFields();  // Clear the input fields
                            } else {
                                Toast.makeText(AddCar.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddCar.this, "Response Parsing Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddCar.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("car_name", carName);
                params.put("car_model", carModel);
                params.put("license_plate", licensePlate);
                // Send the image as a base64 encoded string
                String encodedImage = encodeImageToBase64(carImageUri);
                params.put("car_image", encodedImage);
                return params;
            }
        };

        // Add the request to Volley request queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    // Method to encode the image to Base64 string
    private String encodeImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = getBytesFromInputStream(inputStream);
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Convert input stream to byte array
    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

    // Method to clear the input fields
    private void clearFields() {
        etCarName.setText("");
        etCarModel.setText("");
        licensePlateEditText.setText("");
        imgCarImage.setImageURI(null);  // Clear the image view
    }
}
