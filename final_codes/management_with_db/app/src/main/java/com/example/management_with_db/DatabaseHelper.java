package com.example.management_with_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced DatabaseHelper with Singleton pattern to avoid "database closed" issues.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "ResourceManager.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RESOURCES = "resources";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AVAILABILITY = "availability";
    public static final String COLUMN_DATE = "date";

    private static DatabaseHelper instance;

    /**
     * Singleton pattern ensures we use the same database connection across the app.
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_RESOURCES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_AVAILABILITY + " INTEGER DEFAULT 0, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES);
        onCreate(db);
    }

    public long addResource(String name, int availability, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AVAILABILITY, availability);
        values.put(COLUMN_DATE, date);
        return db.insert(TABLE_RESOURCES, null, values);
    }

    public List<Resource> getAllResources() {
        List<Resource> resourceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RESOURCES + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int availability = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABILITY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                resourceList.add(new Resource(id, name, availability, date));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return resourceList;
    }

    public boolean updateResource(int id, String name, int availability, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AVAILABILITY, availability);
        values.put(COLUMN_DATE, date);
        return db.update(TABLE_RESOURCES, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateAvailability(int id, int newAvailability) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AVAILABILITY, newAvailability);
        return db.update(TABLE_RESOURCES, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteResource(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_RESOURCES, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public void clearAllResources() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESOURCES, null, null);
    }

    public static class Resource {
        public int id;
        public String name;
        public int availability;
        public String date;

        public Resource(int id, String name, int availability, String date) {
            this.id = id;
            this.name = name;
            this.availability = availability;
            this.date = date;
        }

        @Override
        public String toString() {
            return name + " (" + availability + ")";
        }
    }
}
