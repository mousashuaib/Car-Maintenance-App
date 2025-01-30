package com.example.car_main;

import android.app.Activity;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditCar extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText carNameEditText, carModelEditText, licensePlateEditText;
    private ImageView carImageView;
    private Button saveButton, selectImageButton;
    private Uri carImageUri;
    private String carId, carImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        // Initialize UI elements
        carNameEditText = findViewById(R.id.carNameEditText);
        carModelEditText = findViewById(R.id.carModelEditText);
        licensePlateEditText = findViewById(R.id.licensePlateEditText);
        carImageView = findViewById(R.id.carImageView);
        saveButton = findViewById(R.id.saveButton);
        selectImageButton = findViewById(R.id.selectImageButton);

        // Get data from Intent
        Intent intent = getIntent();
        carId = intent.getStringExtra("car_id");
        carNameEditText.setText(intent.getStringExtra("car_name"));
        carModelEditText.setText(intent.getStringExtra("car_model"));
        licensePlateEditText.setText(intent.getStringExtra("license_plate"));
        carImage = intent.getStringExtra("car_image");

        // Load existing car image
        Picasso.get().load(Constants.BASE_URL + "uploads/cars/" + carImage).into(carImageView);


        // Select a new image
        selectImageButton.setOnClickListener(v -> openFileChooser());

        // Save changes
        saveButton.setOnClickListener(v -> saveChanges());
    }

    // Open file chooser to select a new image
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Filter for images
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            carImageUri = data.getData();
            carImageView.setImageURI(carImageUri); // Display selected image in ImageView
        }
    }

    // Save changes to the database
    private void saveChanges() {
        String updateUrl = Constants.BASE_URL + "updatecar.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(EditCar.this, "Car updated successfully!", Toast.LENGTH_SHORT).show();

                                // Send updated data back to ShowCars
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("car_id", carId);
                                resultIntent.putExtra("car_name", carNameEditText.getText().toString());
                                resultIntent.putExtra("car_model", carModelEditText.getText().toString());
                                resultIntent.putExtra("license_plate", licensePlateEditText.getText().toString());

                                if (carImageUri != null) {
                                    // Encode the new image to Base64
                                    String encodedImage = encodeImageToBase64(carImageUri);
                                    resultIntent.putExtra("car_image", encodedImage);
                                } else {
                                    resultIntent.putExtra("car_image", carImage); // Use the existing image
                                }

                                setResult(Activity.RESULT_OK, resultIntent);
                                finish(); // Close the edit activity
                            } else {
                                Toast.makeText(EditCar.this, "Failed to update car!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditCar.this, "Response parsing error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditCar.this, "Network error!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("car_id", carId);
                params.put("car_name", carNameEditText.getText().toString());
                params.put("car_model", carModelEditText.getText().toString());
                params.put("license_plate", licensePlateEditText.getText().toString());

                if (carImageUri != null) {
                    String encodedImage = encodeImageToBase64(carImageUri);
                    params.put("car_image", encodedImage);
                } else {
                    params.put("car_image", carImage);
                }

                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    // Method to encode the image to Base64
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
}
