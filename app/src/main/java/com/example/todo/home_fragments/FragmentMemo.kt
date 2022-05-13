package com.example.todo.home_fragments

import android.content.Context
import android.util.Log
import com.example.todo.R
import java.util.concurrent.CountDownLatch

class FragmentMemo(val listener: StartActivityListener) : FragmentBase(listener) {
    override val title = R.string.main_toolbar_reminder
    private val TAG = "ActivityLoading"

    override fun initFragmentData(context: Context) {
        Log.d(TAG, "initFragmentData: reminder")
    }

    override fun refreshData(context: Context) {
        TODO("Not yet implemented")
    }

}