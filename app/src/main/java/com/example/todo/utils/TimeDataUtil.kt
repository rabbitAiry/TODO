package com.example.todo.utils

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

object TimeDataUtil {
    const val DATE_PREFERENCE = "date"
    const val KEY_LATEST_DATE = "latest_date"
    const val KEY_DATE_LIMIT = "date_limit"
    const val DEFAULT_DATE_LIMIT = 10

    fun getDateText(c: Calendar): String =
        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(c.time)

    fun getDateText(token: Int): String =
        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(parseDateToken(token).time)

    fun getDateText(year: Int, month: Int, dayOfMonth: Int): String {
        val c = Calendar.getInstance()
        c.set(year, month, dayOfMonth)
        return getDateText(c)
    }

    fun getDateToken(c: Calendar): Int =
        c[Calendar.YEAR] * 10000 + (c[Calendar.MONTH] + 1) * 100 + c[Calendar.DATE]

    fun getTodayToken(): Int = getDateToken(Calendar.getInstance())

    fun getDateToken(year: Int, month: Int, dayOfMonth: Int): Int =
        year * 10000 + (month + 1) * 100 + dayOfMonth

    // yyyy/MM/dd
    fun getDateToken(s: String): Int =
        getDateToken(
            s.substring(0, 4).toInt(),
            s.substring(5, 7).toInt(),
            s.substring(8, 10).toInt()
        )

    fun getTodayText(): String =
        getDateText(Calendar.getInstance())

    fun parseDateToken(token: Int): Calendar {
        val c = Calendar.getInstance()
        c.set(token / 10000, token % 10000 / 100 - 1, token % 100);
        return c;
    }

    fun addDate(dayToken: Int, forwardDays: Int, timeUnit: Int = Calendar.DATE): Int {
        val c = parseDateToken(dayToken)
        c.add(timeUnit, forwardDays)
        return getDateToken(c)
    }

    fun addDate(
        dayToken: Int,
        periodTimes: Int,
        periodLeft: Int,
        periodValue: Int,
        periodUnit: Int
    ): Int {
        return addDate(dayToken, (periodTimes - periodLeft) * periodValue, periodUnit)
    }

    fun getTimeText(c: Calendar): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(c.time)

    fun getTimeText(token: Int): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(parseTimeToken(token).time)

    fun getTimeText(hourOfDay: Int, minute: Int): String {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)
        return getTimeText(c)
    }

    fun getNowTimeText(): String = getTimeText(Calendar.getInstance())

    fun getNowTimeToken(): Int = getTimeToken(Calendar.getInstance())

    // e.g: 2200 = 22:00
    fun getTimeToken(s: String): Int {
        val hour = s.substring(0, 2).toInt()
        val minute = s.substring(3, 5).toInt()
        return hour * 100 + minute
    }

    fun getTimeToken(c: Calendar): Int {
        return c[Calendar.HOUR] * 100 + c[Calendar.MINUTE]
    }

    fun getTimeToken(hourOfDay: Int, minute: Int): Int = hourOfDay * 100 + minute

    fun parseTimeToken(token: Int): Calendar {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, token / 100)
        c.set(Calendar.MINUTE, token % 100)
        return c
    }

    private fun getPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(DATE_PREFERENCE, Context.MODE_PRIVATE)

    fun getDateLimit(context: Context): Int =
        getPreferences(context).getInt(KEY_DATE_LIMIT, DEFAULT_DATE_LIMIT)

    fun setDateLimit(context: Context, limit: Int) {
        val editor = context.getSharedPreferences(DATE_PREFERENCE, Context.MODE_PRIVATE).edit()
        editor.putInt(KEY_DATE_LIMIT, limit)
        editor.apply()
    }

    fun getLatestDate(context: Context): Int =
        getPreferences(context).getInt(KEY_LATEST_DATE, getTodayToken())

    fun setLatestDate(context: Context) {
        val editor = context.getSharedPreferences(DATE_PREFERENCE, Context.MODE_PRIVATE).edit()
        editor.putInt(KEY_LATEST_DATE, getTodayToken())
        editor.apply()
    }
}