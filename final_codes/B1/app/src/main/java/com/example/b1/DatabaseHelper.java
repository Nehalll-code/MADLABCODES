package com.example.b1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "admission.db";
    public static final String TABLE_NAME = "admission_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "STUDENT_NAME";
    public static final String COL_3 = "MARKS";
    public static final String COL_4 = "GENDER";
    public static final String COL_5 = "COURSE";
    public static final String COL_6 = "ADMISSION_DATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, STUDENT_NAME TEXT, MARKS TEXT, GENDER TEXT, COURSE TEXT, ADMISSION_DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String marks, String gender, String course, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, marks);
        contentValues.put(COL_4, gender);
        contentValues.put(COL_5, course);
        contentValues.put(COL_6, date);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean updateData(String id, String name, String marks, String gender, String course, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, marks);
        contentValues.put(COL_4, gender);
        contentValues.put(COL_5, course);
        contentValues.put(COL_6, date);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return result > 0;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getLatestData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY ID DESC LIMIT 1", null);
    }
}
