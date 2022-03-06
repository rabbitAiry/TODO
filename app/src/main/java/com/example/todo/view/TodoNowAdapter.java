package com.example.todo.view;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.todo.data.TodoItem;
import com.example.todo.data.TodoItemUtils;

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
        holder.itemToUrgentButton.setVisibility(curr.type!=TodoItemUtils.TYPE_URGENT?View.VISIBLE:View.INVISIBLE);
        holder.itemButtonDone.setVisibility(View.VISIBLE);
        holder.itemButtonDone.setOnClickListener(v -> {
            notifyItemRemoved(adapterPosition);
            itemList.remove(curr);
            listener.onDoneClickListener(curr);
        });
        holder.itemToUrgentButton.setOnClickListener(v -> {
            curr.type = TodoItemUtils.TYPE_URGENT;
            notifyItemChanged(adapterPosition);
            listener.toUrgentClickListener(curr);
        });
    }
}