package com.example.booking_pattern;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BookingPatternDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NOTIFICATION = "notification";

    private static DatabaseHelper instance;

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
        String CREATE_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_AGE + " INTEGER,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_NOTIFICATION + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    public long addBooking(BookingRecord booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, booking.getName());
        values.put(COLUMN_AGE, booking.getAge());
        values.put(COLUMN_EMAIL, booking.getEmail());
        values.put(COLUMN_CATEGORY, booking.getCategory());
        values.put(COLUMN_DATE, booking.getDate());
        values.put(COLUMN_TIME, booking.getTime());
        values.put(COLUMN_TYPE, booking.getType());
        values.put(COLUMN_NOTIFICATION, booking.getNotification());

        return db.insert(TABLE_BOOKINGS, null, values);
    }

    public List<BookingRecord> getAllBookings() {
        List<BookingRecord> bookingList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKINGS + " ORDER BY " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BookingRecord booking = new BookingRecord(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION))
                );
                bookingList.add(booking);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return bookingList;
    }

    public int updateBooking(BookingRecord booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, booking.getName());
        values.put(COLUMN_AGE, booking.getAge());
        values.put(COLUMN_EMAIL, booking.getEmail());
        values.put(COLUMN_CATEGORY, booking.getCategory());
        values.put(COLUMN_DATE, booking.getDate());
        values.put(COLUMN_TIME, booking.getTime());
        values.put(COLUMN_TYPE, booking.getType());
        values.put(COLUMN_NOTIFICATION, booking.getNotification());

        return db.update(TABLE_BOOKINGS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(booking.getId())});
    }

    public void deleteBooking(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKINGS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
