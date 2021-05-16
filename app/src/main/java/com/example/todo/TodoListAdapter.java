package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.data.TodoContract.TodoEntry;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListViewHolder> {
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
        if (!cursor.moveToPosition(position)) return;

        String description = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_ITEM));
        int category = cursor.getInt(cursor.getColumnIndex(TodoEntry.COLUMN_STATUS));
        long id = cursor.getLong(cursor.getColumnIndex(TodoEntry._ID));

        holder.textViewTodoDescription.setText(description);
        int statusColor = 0;
        switch (category) {
            case 0:
                statusColor = ContextCompat.getColor(context, R.color.normal);
                break;
            case 1:
                statusColor = ContextCompat.getColor(context, R.color.urgent);
                holder.imageButtonBecomeUrgent.setVisibility(View.INVISIBLE);
        }
        holder.imageViewTodoStatusBar.setBackgroundColor(statusColor);
        holder.itemView.setTag(id);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDoneClick(id);
            }
        });
        holder.imageButtonBecomeUrgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStatusChangeClick(id, TodoEntry.categoryUrgent);
            }
        });
        holder.imageButtonBecomeCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStatusChangeClick(id, TodoEntry.categoryCold);
            }
        });
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
}

class TodoListViewHolder extends RecyclerView.ViewHolder {
    ImageButton button;
    TextView textViewTodoDescription;
    ImageView imageViewTodoStatusBar;
    ImageButton imageButtonBecomeUrgent;
    ImageButton imageButtonBecomeCold;

    public TodoListViewHolder(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.button_done);
        textViewTodoDescription = itemView.findViewById(R.id.todo_item_description);
        imageViewTodoStatusBar = itemView.findViewById(R.id.todo_item_status_bar);
        imageButtonBecomeUrgent = itemView.findViewById(R.id.become_urgent);
        imageButtonBecomeCold = itemView.findViewById(R.id.become_cold);
    }
}
