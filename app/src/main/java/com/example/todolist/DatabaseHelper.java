package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_LAST_EDITED = "last_edited";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_LAST_EDITED + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long addTask(String title, String details, String lastEdited) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DETAILS, details);
        values.put(COLUMN_LAST_EDITED, lastEdited);
        return db.insert(TABLE_TASKS, null, values);
    }

    public Cursor getAllTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TASKS, null, null, null, null, null, COLUMN_LAST_EDITED + " DESC");
    }

    public int updateTask(int id, String title, String details, String lastEdited) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DETAILS, details);
        values.put(COLUMN_LAST_EDITED, lastEdited);
        return db.update(TABLE_TASKS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TASKS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Task getTaskById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String details = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETAILS));
            String lastEdited = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_EDITED));
            cursor.close();
            return new Task(id, title, details, lastEdited);
        }
        return null;
    }
}
