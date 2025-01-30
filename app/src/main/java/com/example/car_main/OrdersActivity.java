package com.example.car_main;

// File: OrdersActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        LinearLayout ordersLayout = findViewById(R.id.ordersLayout);
        Button btPay = findViewById(R.id.btPay);

        OrdersManager ordersManager = OrdersManager.getInstance();
        List<Service> orders = ordersManager.getOrders();

        if (orders.isEmpty()) {
            // Show a message if there are no orders
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No orders yet!");
            emptyMessage.setTextSize(18);
            emptyMessage.setTypeface(emptyMessage.getTypeface(), android.graphics.Typeface.BOLD);
            emptyMessage.setGravity(android.view.Gravity.CENTER);
            ordersLayout.addView(emptyMessage);
            btPay.setVisibility(android.view.View.GONE); // Hide the Pay button
        } else {
            // Display orders
            double totalPrice = 0.0;

            for (Service service : orders) {
                TextView textView = new TextView(this);
                textView.setText(service.getName() + " - " + service.getPrice());
                textView.setTextSize(18);
                textView.setTypeface(textView.getTypeface(), android.graphics.Typeface.BOLD);
                textView.setGravity(android.view.Gravity.CENTER);
                textView.setPadding(0, 16, 0, 16);
                ordersLayout.addView(textView);

                // Accumulate the total price
                totalPrice += Double.parseDouble(service.getPrice().replace("$", ""));
            }
            final double price = totalPrice;

            // Update the Pay button with the total price
            btPay.setText("Pay $" + totalPrice);
            btPay.setVisibility(android.view.View.VISIBLE);

            // Set onClickListener for the Pay button
            btPay.setOnClickListener(v -> {
                Toast.makeText(this, "Payment of $" + price + " completed!", Toast.LENGTH_SHORT).show();
                ordersManager.clearOrders(); // Clear orders after payment
                finish(); // Close the activity
                Intent intent=new Intent(OrdersActivity.this, PaymentMethod.class);
                intent.putExtra("amount", String.valueOf(price)); // Pass the price as a String
                startActivity(intent);
            });
        }
    }
}

