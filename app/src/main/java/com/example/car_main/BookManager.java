package com.example.car_main;


import java.util.ArrayList;
import java.util.List;

public class BookManager {
    private static BookManager instance;
    private final List<String> bookings; // Stores bookings as "Part - Date"

    private BookManager() {
        bookings = new ArrayList<>();
    }

    public static BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    public void addBooking(String booking) {
        bookings.add(booking); // Add a booking to the list
    }

    public List<String> getBookings() {
        return bookings; // Return all bookings
    }

    public void clearBookings() {
        bookings.clear(); // Clear the bookings
    }
}

