package com.example.car_main;

public class Service {
    private String name;         // Name of the service
    private int imageResId;      // Resource ID for the service image
    private String price;        // Price of the service

    public Service(String name, int imageResId, String price) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getPrice() {
        return price;
    }
}