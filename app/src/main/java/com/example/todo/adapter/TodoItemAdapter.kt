package com.example.todo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.TodoItem
import com.example.todo.utils.TodoTypeUtil
import com.example.todo.view.TypeIndicator

open class TodoItemAdapter(
    var itemList: MutableList<TodoItem>?,
    val context: Context,
    val listener: TodoListener
) : RecyclerView.Adapter<TodoItemHolder>() {

    interface TodoListener {
        fun onItemLongClickListener(item: TodoItem?)
        fun onDoneClickListener(item: TodoItem?)
        fun toUrgentClickListener(item: TodoItem?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemHolder =
        TodoItemHolder(LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false))

    override fun onBindViewHolder(holder: TodoItemHolder, position: Int) {
        val adapterPosition: Int = holder.adapterPosition
        itemList?.let {
            val curr: TodoItem = it.get(adapterPosition)
            holder.itemContentText.text = curr.content
            holder.itemTypeBar.background =
                TypeIndicator(TodoTypeUtil.getTypeColorByTypeId(curr.type!!, context))
            holder.itemToUrgentButton.visibility = View.INVISIBLE
            holder.itemButtonDone.visibility = View.INVISIBLE
            holder.itemView.setOnClickListener { listener.onItemLongClickListener(curr) }
        }
    }

    override fun getItemCount(): Int = if (itemList == null) 0 else itemList!!.size

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(itemList: MutableList<TodoItem>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }
}