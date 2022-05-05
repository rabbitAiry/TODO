package com.example.todo.home_fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.ActivityEditTodo
import com.example.todo.R
import com.example.todo.adapter.TodoItemAdapter
import com.example.todo.adapter.TodoNowAdapter
import com.example.todo.data.TodoItem
import com.example.todo.databinding.FragmentNowTodoBinding
import com.example.todo.utils.ThreadPoolUtil
import com.example.todo.utils.TodoDataUtil
import java.util.concurrent.CountDownLatch

class FragmentNow : FragmentBase() {
    override val title = R.string.main_toolbar_now
    private var _binding: FragmentNowTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoList: MutableList<TodoItem>
    val REQUEST_EDIT = 1
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
            startActivityForResult(intent, REQUEST_EDIT)
        }
        return view
    }

    /**
     * MainActivity会在绑定任意fragment前先初始化其数据
     * 使用CountDownLatch表示当前fragment完成了初始化的任务
     */
    override fun initFragmentData(context: Context, countDownLatch: CountDownLatch) {
        // date check >> get list
        TodoDataUtil.checkForPeriodicTodo(context)
        TodoDataUtil.checkIsTodoOutOfTime(context)
        todoList = TodoDataUtil.getTodoForNowFragment(context)
        countDownLatch.countDown()
    }

    private fun initList() {
        binding.fragmentNowList.layoutManager = LinearLayoutManager(context)
        binding.fragmentNowList.adapter =
            TodoNowAdapter(null, context!!, object : TodoItemAdapter.TodoListener {
                override fun onItemLongClickListener(item: TodoItem?) {
                    val intent = Intent(context, ActivityEditTodo::class.java)
                    intent.putExtra(ActivityEditTodo.TODO_OBJECT, item)
                    intent.putExtra(ActivityEditTodo.mode, ActivityEditTodo.MODE_EDIT)
                    startActivityForResult(intent, REQUEST_EDIT)
                }

                override fun onDoneClickListener(item: TodoItem?) {
                    ThreadPoolUtil.submitTask {
                        item?.let {
                            TodoDataUtil.deleteTodo(
                                it,
                                context!!
                            )
                        }
                    }
                }

                override fun toUrgentClickListener(item: TodoItem?) {
                    ThreadPoolUtil.submitTask {
                        item?.let { TodoDataUtil.updateTodo(context!!, it) }
                        val list = TodoDataUtil.getTodoForNowFragment(context!!)
                        activity?.runOnUiThread { adapter.refreshData(list) }
                    }
                }
            })
        refreshList()
    }

    private fun getList() {
        ThreadPoolUtil.submitTask {
            todoList = TodoDataUtil.getTodoForNowFragment(context!!)
            activity?.runOnUiThread { refreshList() }
        }
    }

    private fun refreshList() {
        adapter.refreshData(todoList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_EDIT) {
                getList()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}