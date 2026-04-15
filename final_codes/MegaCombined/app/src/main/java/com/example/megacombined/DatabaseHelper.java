package com.example.megacombined;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartBooking.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_VEHICLE = "VehicleBookings";
    public static final String TABLE_COURSE = "CourseEnrollments";
    public static final String TABLE_PARKING = "ParkingBookings";

    // Common columns
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_AGE = "age";
    public static final String COL_EMAIL = "email";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";

    // Vehicle columns
    public static final String COL_V_TYPE = "service_type";
    public static final String COL_V_PICKUP = "pickup_required";
    public static final String COL_V_CENTER = "service_center";
    public static final String COL_V_ISSUES = "issues";
    public static final String COL_V_SLOT = "slot";

    // Course columns
    public static final String COL_C_TYPE = "course_type";
    public static final String COL_C_ACCESS = "access_enabled";
    public static final String COL_C_INSTRUCTOR = "instructor";
    public static final String COL_C_MODULES = "modules";
    public static final String COL_C_BATCH = "batch";

    // Parking columns
    public static final String COL_P_V_TYPE = "vehicle_type";
    public static final String COL_P_ACTIVE = "slot_active";
    public static final String COL_P_AREA = "parking_area";
    public static final String COL_P_OPTIONS = "slot_options";
    public static final String COL_P_SHIFT = "shift";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createVehicleTable = "CREATE TABLE " + TABLE_VEHICLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_AGE + " INTEGER, " +
                COL_EMAIL + " TEXT, " +
                COL_V_TYPE + " TEXT, " +
                COL_V_PICKUP + " INTEGER, " +
                COL_V_CENTER + " TEXT, " +
                COL_V_ISSUES + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_V_SLOT + " TEXT)";

        String createCourseTable = "CREATE TABLE " + TABLE_COURSE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_AGE + " INTEGER, " +
                COL_EMAIL + " TEXT, " +
                COL_C_TYPE + " TEXT, " +
                COL_C_ACCESS + " INTEGER, " +
                COL_C_INSTRUCTOR + " TEXT, " +
                COL_C_MODULES + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_C_BATCH + " TEXT)";

        String createParkingTable = "CREATE TABLE " + TABLE_PARKING + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_AGE + " INTEGER, " +
                COL_EMAIL + " TEXT, " +
                COL_P_V_TYPE + " TEXT, " +
                COL_P_ACTIVE + " INTEGER, " +
                COL_P_AREA + " TEXT, " +
                COL_P_OPTIONS + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_P_SHIFT + " TEXT)";

        db.execSQL(createVehicleTable);
        db.execSQL(createCourseTable);
        db.execSQL(createParkingTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING);
        onCreate(db);
    }

    // Insert methods
    public long insertVehicleBooking(ContentValues values) {
        return getWritableDatabase().insert(TABLE_VEHICLE, null, values);
    }

    public long insertCourseEnrollment(ContentValues values) {
        return getWritableDatabase().insert(TABLE_COURSE, null, values);
    }

    public long insertParkingBooking(ContentValues values) {
        return getWritableDatabase().insert(TABLE_PARKING, null, values);
    }

    // Update methods
    public int updateVehicleBooking(int id, ContentValues values) {
        return getWritableDatabase().update(TABLE_VEHICLE, values, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int updateCourseEnrollment(int id, ContentValues values) {
        return getWritableDatabase().update(TABLE_COURSE, values, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int updateParkingBooking(int id, ContentValues values) {
        return getWritableDatabase().update(TABLE_PARKING, values, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Fetch methods
    public Cursor getAllVehicleBookings() {
        return getReadableDatabase().query(TABLE_VEHICLE, null, null, null, null, null, COL_ID + " DESC");
    }

    public Cursor getAllCourseEnrollments() {
        return getReadableDatabase().query(TABLE_COURSE, null, null, null, null, null, COL_ID + " DESC");
    }

    public Cursor getAllParkingBookings() {
        return getReadableDatabase().query(TABLE_PARKING, null, null, null, null, null, COL_ID + " DESC");
    }

    // Delete methods
    public void deleteVehicleBooking(int id) {
        getWritableDatabase().delete(TABLE_VEHICLE, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteCourseEnrollment(int id) {
        getWritableDatabase().delete(TABLE_COURSE, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteParkingBooking(int id) {
        getWritableDatabase().delete(TABLE_PARKING, COL_ID + "=?", new String[]{String.valueOf(id)});
    }
}
