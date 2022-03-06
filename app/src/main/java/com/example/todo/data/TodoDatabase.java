package com.example.todo.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 2, entities = {TodoItem.class}, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();
    private volatile static TodoDatabase instance;

    public static TodoDatabase getInstance(final Context context){
        if(instance == null){
            synchronized (TodoDatabase.class){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            TodoDatabase.class,
                            "todo_database").build();
                }
            }
        }
        return instance;
    }
}
