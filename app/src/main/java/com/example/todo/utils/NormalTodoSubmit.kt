package com.example.todo.utils

import android.content.Context
import com.example.todo.data.PeriodData
import com.example.todo.data.TodoItem

open class NormalTodoSubmit {
    open fun add(context: Context, content: String, type: Int, periodInfo: PeriodData?) {
        ThreadPoolUtil.submitTask {
            TodoDataUtil.insertTodo(context, TodoItem(null, content, type))
        }
    }

    open fun update(
        context: Context,
        todoItem: TodoItem,
        content: String,
        type: Int,
        periodInfo: PeriodData?
    ) {
        ThreadPoolUtil.submitTask {
            todoItem.content = content
            todoItem.type = type
            todoItem.dateAdded = TimeDataUtil.getTodayToken()
            TodoDataUtil.updateTodo(context, todoItem)
        }
    }

    open fun done(todoItem: TodoItem, context: Context) {
        TodoDataUtil.deleteTodo(todoItem, context)
    }
}