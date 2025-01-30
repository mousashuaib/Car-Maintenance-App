package com.example.car_main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private Context context;
    private List<Car> carList;

    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_car_adapter, parent, false);
        return new CarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(holder.getAdapterPosition());
        holder.carName.setText(car.getCarName());
        holder.carModel.setText(car.getCarModel());
        holder.licensePlate.setText(car.getLicensePlate());

        // Generate a unique URL with a timestamp to force fresh loading
        String imageUrl = Constants.BASE_URL + "uploads/cars/" + car.getCarImage() + "?timestamp=" + System.currentTimeMillis();

        // Invalidate the image cache for this URL
        Picasso.get().invalidate(imageUrl);

        // Load the new image
        Picasso.get().load(imageUrl).into(holder.carImage);

        // Handle item click for editing
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditCar.class);
            intent.putExtra("car_id", car.getId());
            intent.putExtra("car_name", car.getCarName());
            intent.putExtra("car_model", car.getCarModel());
            intent.putExtra("license_plate", car.getLicensePlate());
            intent.putExtra("car_image", car.getCarImage());
            ((Activity) context).startActivityForResult(intent, 1);
        });

        // Handle delete button click
        holder.deleteCar.setOnClickListener(v -> deleteCar(car, holder.getAdapterPosition()));
    }



    private void deleteCar(Car car, int position) {
        String deleteUrl = Constants.BASE_URL + "DeleteCar.php?license_plate=" + car.getLicensePlate();
        Log.d("DeleteCarURL", deleteUrl); // Log the URL being used

        StringRequest stringRequest = new StringRequest(Request.Method.GET, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DeleteCarResponse", response); // Log the server response
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {
                                Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();

                                // Remove car from list and notify RecyclerView
                                carList.remove(position);
                                notifyItemRemoved(position);
                            } else {
                                Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DeleteCarError", error.toString()); // Log any Volley error
                        Toast.makeText(context, "Network error. Could not delete car.", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(context).add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView carName, carModel, licensePlate;
        ImageView carImage, deleteCar;

        public CarViewHolder(View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.carName);
            carModel = itemView.findViewById(R.id.carModel);
            licensePlate = itemView.findViewById(R.id.licensePlate);
            carImage = itemView.findViewById(R.id.carImage);
            deleteCar = itemView.findViewById(R.id.deleteCar);
        }
    }
}
