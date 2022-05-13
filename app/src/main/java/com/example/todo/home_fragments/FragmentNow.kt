package com.example.todo.home_fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.ActivityEditTodo
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.adapter.TodoItemAdapter
import com.example.todo.adapter.TodoNowAdapter
import com.example.todo.data.TodoItem
import com.example.todo.databinding.FragmentNowTodoBinding
import com.example.todo.utils.ThreadPoolUtil
import com.example.todo.utils.TimeDataUtil
import com.example.todo.utils.TodoDataUtil
import com.example.todo.utils.TodoWrapper

class FragmentNow(val listener: StartActivityListener) : FragmentBase(listener) {
    override val title = R.string.main_toolbar_now
    private var _binding: FragmentNowTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoList: MutableList<TodoItem>
    private lateinit var adapter: TodoNowAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNowTodoBinding.inflate(inflater, container, false)
        val view: View = binding.root
        initList()
        binding.fragmentNowButtonAdd.setOnClickListener {
            val intent = Intent(context, ActivityEditTodo::class.java)
            intent.putExtra(ActivityEditTodo.mode, ActivityEditTodo.MODE_ADD)
            listener.startActivityForResult(intent, MainActivity.refresh_todo_list_request)
        }
        return view
    }

    override fun initFragmentData(context: Context) {
        val todayToken = TimeDataUtil.getTodayToken()
        if (TimeDataUtil.getLatestDate(context) != todayToken) {
            TodoDataUtil.checkForPeriodicTodo(context, todayToken)
            TodoDataUtil.checkIsTodoOutOfTime(context, todayToken)
            TimeDataUtil.setLatestDate(context, todayToken)
        }
        todoList = TodoDataUtil.getTodoForNowFragment(context)
        if (_binding != null) activity?.runOnUiThread { refreshList() }
    }

    override fun refreshData(context: Context) {
        todoList = TodoDataUtil.getTodoForNowFragment(context)
        if (_binding != null) activity?.runOnUiThread { refreshList() }
    }

    private fun initList() {
        binding.fragmentNowList.layoutManager = LinearLayoutManager(context)
        adapter = TodoNowAdapter(null, context!!, object : TodoItemAdapter.TodoListener {
            override fun onItemLongClickListener(item: TodoItem?) {
                val intent = Intent(context, ActivityEditTodo::class.java)
                intent.putExtra(ActivityEditTodo.TODO_OBJECT, item)
                intent.putExtra(ActivityEditTodo.mode, ActivityEditTodo.MODE_EDIT)
                listener.startActivityForResult(intent, MainActivity.refresh_todo_list_request)
            }

            override fun onItemClickListener(item: TodoItem?) {}

            override fun onDoneClickListener(item: TodoItem?) {
                ThreadPoolUtil.submitTask {
                    item?.let {
                        TodoWrapper.getTypeWrapperById(it.type!!).handler!!.done(
                            it,
                            context!!
                        )
                    }
                }
            }

            override fun toUrgentClickListener(item: TodoItem?) {
                ThreadPoolUtil.submitTask {
                    item?.let { TodoDataUtil.updateTodo(context!!, it) }
                    getAndRefreshList()
                }
            }
        })
        binding.fragmentNowList.adapter = adapter
        refreshList()
    }

    private fun getAndRefreshList() {
        ThreadPoolUtil.submitTask {
            todoList = TodoDataUtil.getTodoForNowFragment(context!!)
            activity?.runOnUiThread { refreshList() }
        }
    }

    private fun refreshList() {
        adapter.refreshData(todoList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}