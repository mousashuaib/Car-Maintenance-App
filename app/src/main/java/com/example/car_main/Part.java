package com.example.car_main;

// File: Part.java
public class Part {
    private String name;
    private int imageResId;
    private String description;

    public Part(String name, int imageResId, String description) {
        this.name = name;
        this.imageResId = imageResId;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDescription() {
        return description;
    }
}

