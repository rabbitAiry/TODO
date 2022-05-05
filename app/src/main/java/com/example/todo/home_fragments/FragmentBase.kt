package com.example.todo.home_fragments

import android.content.Context
import androidx.fragment.app.Fragment
import java.util.concurrent.CountDownLatch

abstract class FragmentBase : Fragment() {
    abstract val title: Int
    abstract fun initFragmentData(context: Context, countDownLatch: CountDownLatch)
}