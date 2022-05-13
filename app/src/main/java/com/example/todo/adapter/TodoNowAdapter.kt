package com.example.todo.adapter

import android.content.Context
import android.util.Log
import android.view.View
import com.example.todo.data.TodoItem
import com.example.todo.utils.TodoTypeUtil

class TodoNowAdapter(itemList: MutableList<TodoItem>?, context: Context, listener: TodoListener) :
    TodoItemAdapter(itemList, context, listener) {

    override fun onBindViewHolder(holder: TodoItemHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val adapterPosition: Int = holder.adapterPosition
        itemList?.let {
            val curr: TodoItem = it[adapterPosition]
            holder.itemToUrgentButton.visibility =
                if (curr.type == TodoTypeUtil.TYPE_NORMAL) View.VISIBLE else View.INVISIBLE
            holder.itemButtonDone.visibility = View.VISIBLE
            holder.itemButtonDone.setOnClickListener { _ ->
                notifyItemRemoved(holder.adapterPosition)
                it.remove(curr)
                listener.onDoneClickListener(curr)
            }
            holder.itemToUrgentButton.setOnClickListener {
                curr.type = TodoTypeUtil.TYPE_URGENT
                listener.toUrgentClickListener(curr)
            }
        }
    }
}