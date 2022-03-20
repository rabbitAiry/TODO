package com.example.todo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.todo.data.TodoItem;
import com.example.todo.data.TodoItemTypeUtils;

import java.util.List;

public class TodoNowAdapter extends TodoItemAdapter {
    public TodoNowAdapter(List<TodoItem> itemList, Context context, TodoListener listener) {
        super(itemList, context, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int adapterPosition = holder.getAdapterPosition();
        TodoItem curr = itemList.get(adapterPosition);
        holder.itemToUrgentButton.setVisibility(curr.type!= TodoItemTypeUtils.TYPE_URGENT?View.VISIBLE:View.INVISIBLE);
        holder.itemButtonDone.setVisibility(View.VISIBLE);
        holder.itemButtonDone.setOnClickListener(v -> {
            notifyItemRemoved(adapterPosition);
            itemList.remove(curr);
            listener.onDoneClickListener(curr);
        });
        holder.itemToUrgentButton.setOnClickListener(v -> {
            curr.type = TodoItemTypeUtils.TYPE_URGENT;
            listener.toUrgentClickListener(curr);
        });
    }
}