package com.example.car_main;

public class SupportMessage {
    private String name;
    private String email;
    private String phone;
    private String message;
    private String createdAt;
    private String subject;

    public SupportMessage(String name, String email, String phone,String subject, String message, String createdAt) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.message = message;
        this.createdAt = createdAt;
        this.subject=subject;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSubject() {
        return subject;
    }
}
