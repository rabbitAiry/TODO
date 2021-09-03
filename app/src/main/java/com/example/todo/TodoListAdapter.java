package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.data.TodoContract.TodoEntry;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {
    private Cursor cursor;
    private Context context;
    final private TodoItemListener listener;

    public TodoListAdapter(Cursor cursor, Context context, TodoItemListener listener) {
        this.cursor = cursor;
        this.context = context;
        this.listener = listener;
    }

    public interface TodoItemListener {
        void onDoneClick(long id);

        void onStatusChangeClick(long id, int becoming);
    }

    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.todo_item, parent, false);

        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    class TodoListViewHolder extends RecyclerView.ViewHolder {
        ImageButton button;
        TextView textViewTodoDescription;
        ImageView imageViewTodoStatusBar;
        ImageButton imageButtonBecomeUrgent;

        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
