package com.example.marksdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "MarksDB.db";
    private static final int DATABASE_VERSION = 1;

    // Users Table
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Marks Table
    public static final String TABLE_MARKS = "marks";
    public static final String COL_MARKS_ID = "id";
    public static final String COL_STUDENT_NAME = "student_name";
    public static final String COL_SUBJ1 = "subj1_marks";
    public static final String COL_SUBJ2 = "subj2_marks";
    public static final String COL_SUBJ3 = "subj3_marks";
    public static final String COL_SUBJ4 = "subj4_marks";
    public static final String COL_SUBJ5 = "subj5_marks";

    // GPA Table
    public static final String TABLE_GPA = "gpa_table";
    public static final String COL_GPA_ID = "id";
    public static final String COL_GPA_STUDENT_NAME = "student_name";
    public static final String COL_GPA_VALUE = "gpa";

    // Settings Table (for Admin changes)
    public static final String TABLE_SETTINGS = "settings";
    public static final String COL_SETTING_ID = "id";
    public static final String COL_SUBJ_INDEX = "subj_index";
    public static final String COL_SUBJ_NAME = "subj_name";
    public static final String COL_SUBJ_CREDITS = "subj_credits";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating tables");
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT)";
        
        String createMarksTable = "CREATE TABLE " + TABLE_MARKS + " (" +
                COL_MARKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_NAME + " TEXT, " +
                COL_SUBJ1 + " INTEGER, " +
                COL_SUBJ2 + " INTEGER, " +
                COL_SUBJ3 + " INTEGER, " +
                COL_SUBJ4 + " INTEGER, " +
                COL_SUBJ5 + " INTEGER)";

        String createGpaTable = "CREATE TABLE " + TABLE_GPA + " (" +
                COL_GPA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_GPA_STUDENT_NAME + " TEXT, " +
                COL_GPA_VALUE + " REAL)";

        String createSettingsTable = "CREATE TABLE " + TABLE_SETTINGS + " (" +
                COL_SETTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SUBJ_INDEX + " INTEGER, " +
                COL_SUBJ_NAME + " TEXT, " +
                COL_SUBJ_CREDITS + " INTEGER)";

        db.execSQL(createUsersTable);
        db.execSQL(createMarksTable);
        db.execSQL(createGpaTable);
        db.execSQL(createSettingsTable);

        // Initialize default subject settings
        for (int i = 1; i <= 5; i++) {
            ContentValues values = new ContentValues();
            values.put(COL_SUBJ_INDEX, i);
            values.put(COL_SUBJ_NAME, "Subject " + i);
            values.put(COL_SUBJ_CREDITS, 3); // Default credits
            db.insert(TABLE_SETTINGS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GPA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        Log.d(TAG, "Registering user: " + username + ", result: " + result);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Log.d(TAG, "Checking user: " + username + ", exists: " + exists);
        return exists;
    }

    public void insertMarks(String name, int[] marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NAME, name);
        values.put(COL_SUBJ1, marks[0]);
        values.put(COL_SUBJ2, marks[1]);
        values.put(COL_SUBJ3, marks[2]);
        values.put(COL_SUBJ4, marks[3]);
        values.put(COL_SUBJ5, marks[4]);
        db.insert(TABLE_MARKS, null, values);
        Log.d(TAG, "Inserted marks for: " + name);
    }

    public void insertGPA(String name, double gpa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_GPA_STUDENT_NAME, name);
        values.put(COL_GPA_VALUE, gpa);
        db.insert(TABLE_GPA, null, values);
        Log.d(TAG, "Inserted GPA for: " + name + " = " + gpa);
    }

    public Cursor getTopper() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GPA + " ORDER BY " + COL_GPA_VALUE + " DESC LIMIT 1", null);
    }

    public Cursor getSettings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_SETTINGS, null, null, null, null, null, COL_SUBJ_INDEX + " ASC");
    }

    public void updateSetting(int index, String name, int credits) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SUBJ_NAME, name);
        values.put(COL_SUBJ_CREDITS, credits);
        db.update(TABLE_SETTINGS, values, COL_SUBJ_INDEX + "=?", new String[]{String.valueOf(index)});
        Log.d(TAG, "Updated setting for subject " + index);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public Cursor getAllMarks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MARKS, null);
    }

    public Cursor getAllGPA() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GPA, null);
    }

    public boolean deleteStudentRecord(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int marksDeleted = db.delete(TABLE_MARKS, COL_STUDENT_NAME + "=?", new String[]{name});
        int gpaDeleted = db.delete(TABLE_GPA, COL_GPA_STUDENT_NAME + "=?", new String[]{name});
        Log.d(TAG, "Deleted records for: " + name + ". Marks: " + marksDeleted + ", GPA: " + gpaDeleted);
        return marksDeleted > 0 || gpaDeleted > 0;
    }
}
