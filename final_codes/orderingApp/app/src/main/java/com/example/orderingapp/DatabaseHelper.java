package com.example.orderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "OrderingDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_CUSTOMER_NAME = "customer_name";
    private static final String COLUMN_CUSTOMER_ADDRESS = "customer_address";
    private static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDER_ID + " TEXT,"
                + COLUMN_CUSTOMER_NAME + " TEXT,"
                + COLUMN_CUSTOMER_ADDRESS + " TEXT,"
                + COLUMN_TOTAL_AMOUNT + " REAL,"
                + COLUMN_TIMESTAMP + " INTEGER" + ")";
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public long addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, order.getOrderId());
        values.put(COLUMN_CUSTOMER_NAME, order.getCustomerName());
        values.put(COLUMN_CUSTOMER_ADDRESS, order.getCustomerAddress());
        values.put(COLUMN_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COLUMN_TIMESTAMP, order.getTimestamp());

        long id = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return id;
    }

    public List<Order> getAllOrders() {
        List<Order> ordersList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " ORDER BY " + COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                // Note: items map is not fully reconstructed here as it requires a separate table for order items
                Order order = new Order(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        null, 
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_ADDRESS))
                );
                ordersList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ordersList;
    }

    public int updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_NAME, order.getCustomerName());
        values.put(COLUMN_CUSTOMER_ADDRESS, order.getCustomerAddress());
        values.put(COLUMN_TOTAL_AMOUNT, order.getTotalAmount());

        return db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + " = ?",
                new String[]{order.getOrderId()});
    }

    public void deleteOrder(String orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, COLUMN_ORDER_ID + " = ?", new String[]{orderId});
        db.close();
    }
}
