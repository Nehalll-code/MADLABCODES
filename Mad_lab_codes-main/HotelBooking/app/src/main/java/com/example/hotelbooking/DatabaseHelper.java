package com.example.hotelbooking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HotelBooking.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ROOM_TYPE = "room_type";
    private static final String COLUMN_BRANCH = "branch";
    private static final String COLUMN_AMENITIES = "amenities";
    private static final String COLUMN_CHECKIN_DATE = "checkin_date";
    private static final String COLUMN_CHECKIN_TIME = "checkin_time";
    private static final String COLUMN_SLOT = "slot";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_AGE + " INTEGER,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_ROOM_TYPE + " TEXT,"
                + COLUMN_BRANCH + " TEXT,"
                + COLUMN_AMENITIES + " TEXT,"
                + COLUMN_CHECKIN_DATE + " TEXT,"
                + COLUMN_CHECKIN_TIME + " TEXT,"
                + COLUMN_SLOT + " TEXT" + ")";
        db.execSQL(CREATE_BOOKINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    public long addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, booking.getName());
        values.put(COLUMN_AGE, booking.getAge());
        values.put(COLUMN_EMAIL, booking.getEmail());
        values.put(COLUMN_ROOM_TYPE, booking.getRoomType());
        values.put(COLUMN_BRANCH, booking.getBranch());
        values.put(COLUMN_AMENITIES, booking.getAmenities());
        values.put(COLUMN_CHECKIN_DATE, booking.getCheckInDate());
        values.put(COLUMN_CHECKIN_TIME, booking.getCheckInTime());
        values.put(COLUMN_SLOT, booking.getSlot());

        long id = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return id;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKINGS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking();
                booking.setId(cursor.getInt(0));
                booking.setName(cursor.getString(1));
                booking.setAge(cursor.getInt(2));
                booking.setEmail(cursor.getString(3));
                booking.setRoomType(cursor.getString(4));
                booking.setBranch(cursor.getString(5));
                booking.setAmenities(cursor.getString(6));
                booking.setCheckInDate(cursor.getString(7));
                booking.setCheckInTime(cursor.getString(8));
                booking.setSlot(cursor.getString(9));
                bookingList.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookingList;
    }

    public void deleteBooking(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKINGS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
