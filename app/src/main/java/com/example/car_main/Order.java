package com.example.car_main;

public class Order {
    private String userName;
    private String serviceName;
    private String servicePrice;
    private String createdAt;

    public Order(String userName, String serviceName, String servicePrice, String createdAt) {
        this.userName = userName;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
