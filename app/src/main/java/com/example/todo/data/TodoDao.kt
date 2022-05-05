package com.example.todo.data

import androidx.room.*
import com.example.todo.utils.TodoTypeUtil

@Dao
interface TodoDao {
    @Insert
    fun insertTodo(item: TodoItem): Long

    @Update
    fun updateTodo(item: TodoItem)

    @Delete
    fun deleteTodo(item: TodoItem)

    @Query("select * from TodoItem where periodDone = 0")
    fun loadAllTodoItem(): List<TodoItem>

    @Query("select * from TodoItem where type not in (:arg1) order by type")
    fun loadNowTodoItem(arg1: Int = TodoTypeUtil.TYPE_LATER): MutableList<TodoItem>

    @Query("select * from TodoItem where type not in (:arg1, :arg2) order by type")
    fun loadNormalAndUrgentItem(
        arg1: Int = TodoTypeUtil.TYPE_URGENT,
        arg2: Int = TodoTypeUtil.TYPE_NORMAL
    ): MutableList<TodoItem>

    @Query("select * from TodoItem where type = :type")
    fun loadSpecifiedTypeTodoItem(type: Int): List<TodoItem>

    @Query("select * from TodoItem where periodDone = 1")
    fun loadPeriodDoneTodoItem(): List<TodoItem>

    @Query("select * from TodoItem where id = :id")
    fun loadSpecifiedIdTodoItem(id: Long): TodoItem
}