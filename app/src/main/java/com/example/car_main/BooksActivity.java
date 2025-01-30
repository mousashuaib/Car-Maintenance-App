package com.example.car_main;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        LinearLayout booksLayout = findViewById(R.id.booksLayout);

        // Get the list of bookings from BookManager
        BookManager bookManager = BookManager.getInstance();

        if (bookManager.getBookings().isEmpty()) {
            // Show a message if there are no bookings
            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No bookings available.");
            emptyMessage.setTextSize(18);
            emptyMessage.setTypeface(emptyMessage.getTypeface(), android.graphics.Typeface.BOLD);
            emptyMessage.setGravity(android.view.Gravity.CENTER);
            booksLayout.addView(emptyMessage);
        } else {
            // Display each booking
            for (String booking : bookManager.getBookings()) {
                TextView textView = new TextView(this);
                textView.setText(booking); // Booking in "Part - Date" format
                textView.setTextSize(16);
                textView.setTypeface(textView.getTypeface(), android.graphics.Typeface.BOLD);
                textView.setPadding(0, 8, 0, 8);
                booksLayout.addView(textView);
            }
        }
    }
}
