package com.example.car_main;

import java.util.ArrayList;
import java.util.List;

public class OrdersManager {
    private static OrdersManager instance;
    private final List<Service> orders;

    private OrdersManager() {
        orders = new ArrayList<>();
    }

    public static OrdersManager getInstance() {
        if (instance == null) {
            instance = new OrdersManager();
        }
        return instance;
    }

    public void addOrder(Service service) {
        orders.add(service);
    }

    public List<Service> getOrders() {
        return orders;
    }

    public void clearOrders() {
        orders.clear();
    }
}