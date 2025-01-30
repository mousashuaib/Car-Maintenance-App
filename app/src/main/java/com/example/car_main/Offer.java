package com.example.car_main;

public class Offer {
    private String title;
    private String description;
    private String expiryDate;

    public Offer(String title, String description, String expiryDate) {
        this.title = title;
        this.description = description;
        this.expiryDate = expiryDate;
    }
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getExpiryDate() {return expiryDate;}
    public void setExpiryDate(String expiryDate) {this.expiryDate = expiryDate;}
}