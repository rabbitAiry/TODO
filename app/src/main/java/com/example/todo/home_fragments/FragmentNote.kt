package com.example.todo.home_fragments

import android.content.Context
import android.util.Log
import com.example.todo.R
import java.util.concurrent.CountDownLatch

class FragmentNote(val listener: StartActivityListener) : FragmentBase(listener) {
    override val title = R.string.main_toolbar_note
    private val TAG = "ActivityLoading"


    override fun initFragmentData(context: Context) {
        Log.d(TAG, "initFragmentData: Me")
    }

    override fun refreshData(context: Context) {
        TODO("Not yet implemented")
    }

}