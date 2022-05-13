package com.example.todo.utils

import android.util.Log
import java.util.concurrent.*

object ThreadPoolUtil {
    private val TAG = "ActivityLoading"
    private val pool: ThreadPoolExecutor =
        Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
        ) as ThreadPoolExecutor
    init {
        // Todo:    仍无法捕获异常
        pool.threadFactory = ThreadFactory { r ->
            val thread = Thread(r)
            thread.uncaughtExceptionHandler =
                Thread.UncaughtExceptionHandler { _, e ->
                    Log.d(TAG, "uncaughtException: ")
                    e.printStackTrace()
                }
            thread
        }
    }

    fun submitTask(runnable: Runnable) {
        pool.submit(runnable)
    }
}