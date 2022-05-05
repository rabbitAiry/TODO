package com.example.todo.utils

import com.example.todo.utils.TodoTypeUtil
import com.example.todo.R
import com.example.todo.utils.PeriodInfo

enum class PeriodInfo(var typeId: Int, var textId: Int) {
    PERIOD_DAY(TodoTypeUtil.PERIOD_DAY, R.string.day),
    PERIOD_WEEK(TodoTypeUtil.PERIOD_WEEK, R.string.week),
    PERIOD_MONTH(TodoTypeUtil.PERIOD_MONTH, R.string.month),
    PERIOD_YEAR(TodoTypeUtil.PERIOD_YEAR, R.string.year);

    companion object {
        fun getPeriodInfo(typeId: Int): PeriodInfo? {
            val infos = values()
            for (info in infos) {
                if (info.typeId == typeId) return info
            }
            return null
        }
    }
}