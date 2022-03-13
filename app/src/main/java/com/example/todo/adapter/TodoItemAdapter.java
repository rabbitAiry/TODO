package com.example.todo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.data.TodoItem;
import com.example.todo.data.TodoItemTypeUtils;
import com.example.todo.view.TypeIndicator;

import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemViewHolder> {
    public List<TodoItem> itemList;
    private final Context context;
    protected TodoListener listener;

    public TodoItemAdapter(List<TodoItem> itemList, Context context, TodoListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;
    }

    public interface TodoListener {
        void onItemLongClickListener(TodoItem item);
        void onDoneClickListener(TodoItem item);
        void toUrgentClickListener(TodoItem item);
    }

    @NonNull
    @Override
    public TodoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        return new TodoItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        TodoItem curr = itemList.get(adapterPosition);
        holder.itemContentText.setText(curr.content);
        holder.itemTypeBar.setBackground(new TypeIndicator(TodoItemTypeUtils.getTypeColorByTypeId(curr.type, context)));
        holder.itemPeriodText.setVisibility(curr.created?View.VISIBLE:View.INVISIBLE);
        holder.itemToUrgentButton.setVisibility(View.INVISIBLE);
        holder.itemButtonDone.setVisibility(View.INVISIBLE);
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClickListener(curr);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return itemList==null?0:itemList.size();
    }

    public void refreshData(List<TodoItem> itemList){
        this.itemList = itemList;
        notifyDataSetChanged();
    }
}
