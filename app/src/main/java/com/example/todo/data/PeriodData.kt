package com.example.todo.data

data class PeriodData(
    val startTime: Int,
    val periodUnit: Int,
    val periodValue: Int,
    val periodTimes: Int,
    val periodLeft: Int = periodUnit,
    val periodDone: Boolean = false
)
