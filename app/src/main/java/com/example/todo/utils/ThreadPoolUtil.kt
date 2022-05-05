package com.example.todo.utils

import java.util.concurrent.*

object ThreadPoolUtil {
    private val pool: ThreadPoolExecutor =
        Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
        ) as ThreadPoolExecutor

    init {
        Runtime.getRuntime().addShutdownHook(Thread { pool.shutdown() })
    }

    fun submitTask(runnable: Runnable) {
        pool.submit(runnable)
    }
}