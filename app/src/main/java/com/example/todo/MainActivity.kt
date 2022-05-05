package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.home_fragments.FragmentMe
import com.example.todo.home_fragments.FragmentNote
import com.example.todo.home_fragments.FragmentNow
import com.example.todo.home_fragments.FragmentReminder
import com.example.todo.utils.ThreadPoolUtil
import com.example.todo.view.ActivityMainContainer
import java.util.concurrent.CountDownLatch

class MainActivity : AppCompatActivity() {
    private val TAG = "cannot open main"
    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: FragmentManager
    private var fragmentIndex = 0
    private val fragmentArr = arrayOf(
        FragmentNow(),
        FragmentNote(),
        FragmentReminder(),
        FragmentMe()
    )

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initFragment()
        Log.d(TAG, "onCreate: ")
        // TODO：逐个初始化
        // TODO: 实现标题
        binding.mainContainer.setSwipeListener(object : ActivityMainContainer.LayoutSwipeListener {
            override fun swapCursor(toward: Int) {
                var index = fragmentIndex + toward
                if (index >= fragmentArr.size) index = fragmentArr.size - 1
                if (index < 0) index = 0
                fragmentIndex = index
                manager.beginTransaction()
                    .replace(R.id.main_container, fragmentArr[index])
                    .commit()
                binding.mainToolbarTextview.text =
                    resources.getText(fragmentArr[fragmentIndex].title)
            }
        })
    }

    private fun initFragment() {
        binding.mainToolbarTextview.text = "加载中"
        ThreadPoolUtil.submitTask {
            val latch = CountDownLatch(fragmentArr.size)
            for (fragment in fragmentArr) {
                ThreadPoolUtil.submitTask { fragment.initFragmentData(this, latch) }
            }
            latch.await()
            runOnUiThread {
                manager = supportFragmentManager
                manager.beginTransaction()
                    .add(R.id.main_container, fragmentArr[0])
                    .commit()
                binding.mainToolbarTextview.text =
                    resources.getText(fragmentArr[fragmentIndex].title)
            }
        }
    }
}