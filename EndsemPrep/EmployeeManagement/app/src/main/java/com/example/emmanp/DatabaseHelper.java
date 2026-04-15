package com.example.emmanp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workforce.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_EMPLOYEES = "employees";

    // Task columns
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_EMPLOYEE = "employee";
    public static final String COLUMN_TASK_DEPARTMENT = "department";
    public static final String COLUMN_TASK_PRIORITY = "priority";
    public static final String COLUMN_TASK_DEADLINE = "deadline";
    public static final String COLUMN_TASK_TIME = "time";
    public static final String COLUMN_TASK_EQUIPMENT_REQUIRED = "equipment_required";
    public static final String COLUMN_TASK_EQUIPMENT_NAME = "equipment_name";
    public static final String COLUMN_TASK_URGENT = "urgent";
    public static final String COLUMN_TASK_NOTES = "notes";
    public static final String COLUMN_TASK_STATUS = "status"; // Pending, Completed

    // Employee columns
    public static final String COLUMN_EMPLOYEE_ID = "id";
    public static final String COLUMN_EMPLOYEE_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createEmployeesTable = "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                COLUMN_EMPLOYEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMPLOYEE_NAME + " TEXT)";
        db.execSQL(createEmployeesTable);

        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_TITLE + " TEXT, " +
                COLUMN_TASK_EMPLOYEE + " TEXT, " +
                COLUMN_TASK_DEPARTMENT + " TEXT, " +
                COLUMN_TASK_PRIORITY + " TEXT, " +
                COLUMN_TASK_DEADLINE + " TEXT, " +
                COLUMN_TASK_TIME + " TEXT, " +
                COLUMN_TASK_EQUIPMENT_REQUIRED + " INTEGER, " +
                COLUMN_TASK_EQUIPMENT_NAME + " TEXT, " +
                COLUMN_TASK_URGENT + " INTEGER, " +
                COLUMN_TASK_NOTES + " TEXT, " +
                COLUMN_TASK_STATUS + " TEXT DEFAULT 'Pending')";
        db.execSQL(createTasksTable);

        // Seed employees
        seedEmployees(db);
    }

    private void seedEmployees(SQLiteDatabase db) {
        String[] employees = {"John Doe", "Jane Smith", "Mike Ross", "Rachel Zane", "Harvey Specter"};
        for (String name : employees) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMPLOYEE_NAME, name);
            db.insert(TABLE_EMPLOYEES, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        onCreate(db);
    }

    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EMPLOYEES, null, null, null, null, null, null);
    }
}
