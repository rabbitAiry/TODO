package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.adapter.TodoItemAdapter
import com.example.todo.data.TodoItem
import com.example.todo.databinding.ActivityAllItemBinding
import com.example.todo.utils.ThreadPoolUtil
import com.example.todo.utils.TodoDataUtil
import com.example.todo.utils.TodoTypeUtil

class ActivityAllItem : AppCompatActivity() {
    lateinit var adapter: TodoItemAdapter
    var typeListOnDisplay = TodoTypeUtil.TYPE_ALL
    val REQUEST_EDIT = 1
    val MESSAGE_UPDATE = 2
    val handler = Handler(Looper.myLooper()!!) {
        if (it.what == MESSAGE_UPDATE) {
            val list = it.obj as MutableList<TodoItem>
            adapter.refreshData(list)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAllItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(R.string.all_item)
        }
        binding.allItemList.layoutManager = LinearLayoutManager(this)
        adapter = TodoItemAdapter(null, this, object : TodoItemAdapter.TodoListener {
            override fun onItemLongClickListener(item: TodoItem?) {
                val intent = Intent(this@ActivityAllItem, ActivityEditTodo::class.java)
                intent.putExtra(ActivityEditTodo.TODO_OBJECT, item)
                intent.putExtra(ActivityEditTodo.mode, ActivityEditTodo.MODE_EDIT)
                startActivityForResult(intent, REQUEST_EDIT)
            }

            override fun onDoneClickListener(item: TodoItem?) {}
            override fun toUrgentClickListener(item: TodoItem?) {}
        })
        binding.allItemList.adapter = adapter
        updateList(typeListOnDisplay)
        binding.fragmentAllRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            updateList(
                checkedId
            )
        }
    }

    fun updateList(type: Int) {
        typeListOnDisplay = type
        ThreadPoolUtil.submitTask {
            handler.sendMessage(
                Message.obtain(
                    handler,
                    MESSAGE_UPDATE,
                    TodoDataUtil.getSpecifiedTypeTodo(
                        type,
                        this
                    )
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_OK) {
            if (requestCode == REQUEST_EDIT) updateList(typeListOnDisplay)
        }
    }

}