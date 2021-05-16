package com.example.todo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.data.TodoContract.TodoEntry;

import androidx.annotation.Nullable;

public class TodoHelper extends SQLiteOpenHelper {
    public static final int version = 1;
    public static final String name = "todo.db";

    public TodoHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    private static final String create = "CREATE TABLE " + TodoEntry.TABLE_NAME + " (" + TodoEntry._ID +
            " INTEGER PRIMARY KEY," + TodoEntry.COLUMN_ITEM + " TEXT," + TodoEntry.COLUMN_STATUS + " INTEGER)";

    private static final String delete = "DROP TABLE IF EXISTS " + TodoEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(delete);
        onCreate(db);
    }
}
