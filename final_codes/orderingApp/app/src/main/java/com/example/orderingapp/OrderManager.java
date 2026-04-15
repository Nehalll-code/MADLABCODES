package com.example.orderingapp;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static OrderManager instance;
    private DatabaseHelper dbHelper;

    private OrderManager(Context context) {
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public static synchronized OrderManager getInstance(Context context) {
        if (instance == null) {
            instance = new OrderManager(context);
        }
        return instance;
    }

    // For backward compatibility with existing code that doesn't pass context
    // but this is risky if not initialized. 
    // Usually, we should initialize it in an Application class or first Activity.
    public static OrderManager getInstance() {
        return instance;
    }

    public void addOrder(Order order) {
        dbHelper.addOrder(order);
    }

    public List<Order> getOrders() {
        return dbHelper.getAllOrders();
    }

    public void deleteOrder(String orderId) {
        dbHelper.deleteOrder(orderId);
    }
}