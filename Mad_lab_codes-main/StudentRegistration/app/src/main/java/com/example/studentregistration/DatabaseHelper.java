package com.example.studentregistration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "student_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_COURSE = "course";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DEPARTMENT = "department";
    private static final String COLUMN_SUBJECTS = "subjects";
    private static final String COLUMN_DATE = "admission_date";
    private static final String COLUMN_SECTION = "section";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_AGE + " INTEGER,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_COURSE + " TEXT,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_DEPARTMENT + " TEXT,"
                + COLUMN_SUBJECTS + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_SECTION + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    public long addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_AGE, student.getAge());
        values.put(COLUMN_EMAIL, student.getEmail());
        values.put(COLUMN_COURSE, student.getCourse());
        values.put(COLUMN_STATUS, student.getStatus());
        values.put(COLUMN_DEPARTMENT, student.getDepartment());
        values.put(COLUMN_SUBJECTS, student.getSubjects());
        values.put(COLUMN_DATE, student.getAdmissionDate());
        values.put(COLUMN_SECTION, student.getSection());

        long id = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return id;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                student.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                student.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)));
                student.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                student.setCourse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE)));
                student.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                student.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTMENT)));
                student.setSubjects(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECTS)));
                student.setAdmissionDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                student.setSection(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SECTION)));
                students.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return students;
    }
}
