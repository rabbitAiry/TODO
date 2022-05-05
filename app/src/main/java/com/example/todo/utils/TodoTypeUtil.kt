package com.example.todo.utils

import android.content.Context
import java.util.*

object TodoTypeUtil {
    const val TYPE_ALL = 0
    const val TYPE_URGENT = 1
    const val TYPE_PERIODIC = 2
    const val TYPE_NORMAL = 3
    const val TYPE_LATER = 5
    const val NON_PERIOD = 0
    const val PERIOD_DAY = Calendar.DATE
    const val PERIOD_WEEK = Calendar.WEEK_OF_MONTH
    const val PERIOD_MONTH = Calendar.MONTH
    const val PERIOD_YEAR = Calendar.YEAR

    fun getTypeColorByTypeId(typeId: Int, context: Context): Int {
        return getColor(TodoWrapper.getTypeWrapperById(typeId).typeColorId, context)
    }

    fun getColor(typeColorId: Int, context: Context): Int {
        return context.resources.getColor(typeColorId, context.theme)
    }

    fun getText(typeTextId: Int, context: Context): String {
        return context.resources.getString(typeTextId)
    }
}