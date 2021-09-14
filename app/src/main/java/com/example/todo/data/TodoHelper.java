package com.example.todo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.data.TodoContract.TodoEntry;

import androidx.annotation.Nullable;

public class TodoHelper extends SQLiteOpenHelper {
    public static final int VERSION = 2;
    public static final String TODO_DB = "todo.db";

    public TodoHelper(@Nullable Context context) {
        super(context, TODO_DB, null, VERSION);
    }

    private static final String CREATE_TABLE_TODO = "CREATE TABLE " + TodoEntry.TABLE_TODO + " (" + TodoEntry._ID +
            " INTEGER PRIMARY KEY," + TodoEntry.TODO_COLUMN_CONTENT + " TEXT," + TodoEntry.TODO_COLUMN_TYPE + " TINYINT,"
            + TodoEntry.TODO_COLUMN_IS_CREATED_BY_DAILY + " TINYINT)";

    private static final String DELETE_TABLE_TODO = "DROP TABLE IF EXISTS " + TodoEntry.TABLE_TODO;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TODO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_TODO);
        onCreate(db);
    }
}
