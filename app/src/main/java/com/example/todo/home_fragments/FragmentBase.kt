package com.example.todo.home_fragments

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import java.util.concurrent.CountDownLatch

abstract class FragmentBase(listener: StartActivityListener) : Fragment() {
    abstract val title: Int

    /**
     * MainActivity会在绑定任意fragment前先初始化其数据
     */
    abstract fun initFragmentData(context: Context)

    /**
     * MainActivity在知道该Fragment所展示的数据发生改变时，
     * 调用该方法更新数据源
     */
    abstract fun refreshData(context: Context)
    interface StartActivityListener {
        fun startActivityForResult(intent: Intent, request: Int)
    }
}