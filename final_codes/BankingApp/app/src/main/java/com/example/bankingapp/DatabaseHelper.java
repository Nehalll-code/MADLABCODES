package com.example.bankingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BankingDB";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_CUSTOMERS = "customers";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ACC_TYPE = "account_type";
    private static final String COLUMN_KYC = "kyc_verified";
    private static final String COLUMN_BRANCH = "branch";
    private static final String COLUMN_SERVICES = "services";
    private static final String COLUMN_DATE = "opening_date";
    private static final String COLUMN_TIME = "opening_time";
    private static final String COLUMN_MODE = "account_mode";

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CUSTOMERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_AGE + " INTEGER,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_ACC_TYPE + " TEXT,"
                + COLUMN_KYC + " INTEGER,"
                + COLUMN_BRANCH + " TEXT,"
                + COLUMN_SERVICES + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_MODE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, customer.getName());
        values.put(COLUMN_AGE, customer.getAge());
        values.put(COLUMN_EMAIL, customer.getEmail());
        values.put(COLUMN_ACC_TYPE, customer.getAccountType());
        values.put(COLUMN_KYC, customer.isKycVerified() ? 1 : 0);
        values.put(COLUMN_BRANCH, customer.getBranch());
        values.put(COLUMN_SERVICES, customer.getServices());
        values.put(COLUMN_DATE, customer.getOpeningDate());
        values.put(COLUMN_TIME, customer.getOpeningTime());
        values.put(COLUMN_MODE, customer.getAccountMode());

        long id = db.insert(TABLE_CUSTOMERS, null, values);
        db.close();
        return id;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                customer.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                customer.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)));
                customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                customer.setAccountType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACC_TYPE)));
                customer.setKycVerified(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KYC)) == 1);
                customer.setBranch(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRANCH)));
                customer.setServices(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICES)));
                customer.setOpeningDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                customer.setOpeningTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)));
                customer.setAccountMode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODE)));
                customerList.add(customer);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return customerList;
    }

    public int updateCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, customer.getName());
        values.put(COLUMN_AGE, customer.getAge());
        values.put(COLUMN_EMAIL, customer.getEmail());
        values.put(COLUMN_ACC_TYPE, customer.getAccountType());
        values.put(COLUMN_KYC, customer.isKycVerified() ? 1 : 0);
        values.put(COLUMN_BRANCH, customer.getBranch());
        values.put(COLUMN_SERVICES, customer.getServices());
        values.put(COLUMN_DATE, customer.getOpeningDate());
        values.put(COLUMN_TIME, customer.getOpeningTime());
        values.put(COLUMN_MODE, customer.getAccountMode());

        return db.update(TABLE_CUSTOMERS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(customer.getId())});
    }

    public void deleteCustomer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMERS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
