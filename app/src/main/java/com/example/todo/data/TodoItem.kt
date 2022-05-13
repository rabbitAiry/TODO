package com.example.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todo.utils.TimeDataUtil
import com.example.todo.utils.TodoTypeUtil
import java.io.Serializable

@Entity
data class TodoItem @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var content: String?,
    var type: Int?,
    var periodDone: Boolean = false,
    var periodUnit: Int = TodoTypeUtil.NON_PERIOD,
    var periodValue: Int = 0,
    var periodTimes: Int = 0,
    var periodTimesLeft: Int = 0,
    var dateAdded: Int = TimeDataUtil.getTodayToken(),
    var remind: Boolean = false,
    var remindTime: Int = 0
) : Serializable