package com.example.todo.utils

import android.content.Context
import com.example.todo.data.PeriodData
import com.example.todo.data.TodoItem
import com.example.todo.utils.TimeDataUtil

class PeriodicTodoSubmit : NormalTodoSubmit() {
    override fun add(context: Context, content: String, type: Int, periodInfo: PeriodData?) {
        periodInfo?.let {
            ThreadPoolUtil.submitTask {
                val periodDone = TimeDataUtil.getTodayToken() < it.startTime    // 未开始
                TodoDataUtil.insertTodo(
                    context, TodoItem(
                        null,
                        content,
                        type,
                        periodDone,
                        it.periodUnit,
                        it.periodValue,
                        it.periodTimes,
                        it.periodLeft,
                        it.startTime
                    )
                )
            }
        }
    }

    override fun update(
        context: Context,
        todoItem: TodoItem,
        content: String,
        type: Int,
        periodInfo: PeriodData?
    ) {
        ThreadPoolUtil.submitTask {
            periodInfo?.let {
                todoItem.content = content
                if (it.startTime != todoItem.dateAdded) {
                    todoItem.periodTimesLeft = it.periodTimes
                } else if (it.periodTimes != todoItem.periodTimes) {
                    todoItem.periodTimesLeft += it.periodTimes - todoItem.periodTimes
                }
                val nextPeriod = TimeDataUtil.addDate(
                    TimeDataUtil.getTodayToken(),
                    it.periodTimes,
                    it.periodLeft,
                    it.periodValue,
                    it.periodUnit
                )
                todoItem.periodDone = nextPeriod > TimeDataUtil.getTodayToken() // 下一次未开始
                todoItem.periodUnit = it.periodUnit
                todoItem.periodValue = it.periodValue
                todoItem.periodTimes = it.periodTimes
                todoItem.dateAdded = it.startTime
                if (todoItem.type != TodoTypeUtil.TYPE_PERIODIC) {
                    todoItem.type = type
                }
            }
            TodoDataUtil.updateTodo(context, todoItem)
        }
    }

    override fun done(todoItem: TodoItem, context: Context) {
        todoItem.periodTimesLeft--
        todoItem.periodDone = true
        if (todoItem.periodTimesLeft != 0) TodoDataUtil.updateTodo(context, todoItem)
        else TodoDataUtil.deleteTodo(todoItem, context)
    }
}