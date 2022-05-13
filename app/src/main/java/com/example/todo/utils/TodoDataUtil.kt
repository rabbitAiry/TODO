package com.example.todo.utils

import android.content.Context
import android.util.Log
import com.example.todo.data.TodoDao
import com.example.todo.data.TodoDatabase
import com.example.todo.data.TodoItem

object TodoDataUtil {
    private fun getTodoDao(context: Context): TodoDao {
        val db = TodoDatabase.getInstance(context)
        return db.todoDao()
    }

    fun insertTodo(context: Context, item: TodoItem): Long =
        getTodoDao(context).insertTodo(item)

    fun updateTodo(context: Context, item: TodoItem) =
        getTodoDao(context).updateTodo(item)

    fun deleteTodo(item: TodoItem, context: Context) =
        getTodoDao(context).deleteTodo(item)

    fun getAllTodo(context: Context): List<TodoItem> =
        getTodoDao(context).loadAllTodoItem()

    fun getTodoForNowFragment(context: Context): MutableList<TodoItem> =
        getTodoDao(context).loadNowTodoItem()

    fun getSpecifiedTypeTodo(type: Int, context: Context): List<TodoItem> {
        return if (type == TodoTypeUtil.TYPE_ALL) getAllTodo(context)
        else getTodoDao(context).loadSpecifiedTypeTodoItem(type)
    }

    fun checkForPeriodicTodo(context: Context, todayToken: Int = TimeDataUtil.getTodayToken()) {
        val list = getSpecifiedTypeTodo(TodoTypeUtil.TYPE_PERIODIC, context)
        for (todo in list) {
            if (TimeDataUtil.addDate(
                    todo.dateAdded, todo.periodTimes, todo.periodTimesLeft,
                    todo.periodValue, todo.periodUnit
                ) <= todayToken
            ) {
                if (todo.periodDone) {
                    todo.periodDone = false
                } else {
                    todo.periodTimesLeft--
                    if (todo.periodTimesLeft in 0..1) {
                        todo.type = TodoTypeUtil.TYPE_NORMAL
                        todo.dateAdded = todayToken
                    }
                }
                updateTodo(context, todo)
            }
        }
    }

    fun checkIsTodoOutOfTime(
        context: Context,
        todayToken: Int = TimeDataUtil.getTodayToken()
    ): Boolean {
        val list = getTodoDao(context).loadNormalAndUrgentItem()
        val limit = TimeDataUtil.getDateLimit(context)
        var deleted = false
        for (todo in list) {
            if (todayToken > TimeDataUtil.addDate(todo.dateAdded, limit)) {
                todo.type = TodoTypeUtil.TYPE_LATER
                updateTodo(context, todo)
                deleted = true
            }
        }
        return deleted
    }
}