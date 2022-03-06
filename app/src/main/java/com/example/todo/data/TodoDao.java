package com.example.todo.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    long insertTodo(TodoItem item);

    @Update
    void updateTodo(TodoItem item);

    @Delete
    void deleteTodo(TodoItem item);

    @Query("select * from TodoItem where created = 0")
    List<TodoItem> loadAllTodoItem();

    @Query("select * from TodoItem where type in (:arg1, :arg2) order by type")
    List<TodoItem> loadNowTodoItem(int arg1, int arg2);

    @Query("select * from TodoItem where type = :type and created = 0 order by type")
    List<TodoItem> loadSpecifiedTypeTodoItem(int type);

    @Query("select * from TodoItem where id = :id")
    TodoItem loadSpecifiedIdTodoItem(long id);
}
