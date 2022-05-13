package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.home_fragments.*
import com.example.todo.utils.ThreadPoolUtil
import com.example.todo.view.ActivityMainContainer
import java.util.concurrent.CountDownLatch

class MainActivity : AppCompatActivity() {
    companion object {
        const val refresh_todo_list_request = 10
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: FragmentManager
    private val listener = StartActivityListenerInMainActivity()
    private var fragmentIndex = 0
    private val fragmentArr = arrayOf(
        FragmentNow(listener),
        FragmentNote(listener),
        FragmentMemo(listener),
        FragmentMe(listener)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initFragment()
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
                ThreadPoolUtil.submitTask {
                    fragment.initFragmentData(this)
                    latch.countDown()
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == refresh_todo_list_request) {
            ThreadPoolUtil.submitTask { fragmentArr[0].refreshData(this) }
        }
    }

    inner class StartActivityListenerInMainActivity : FragmentBase.StartActivityListener {
        override fun startActivityForResult(intent: Intent, request: Int) {
            this@MainActivity.startActivityForResult(intent, request)
        }
    }
}