package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.data.TodoContract.TodoEntry;

public class ColdTodoAdapter extends RecyclerView.Adapter<ColdTodoAdapter.ColdTodoViewHolder> {
    private Context context;
    private Cursor cursor;
    private ColdItemListener listener;

    public ColdTodoAdapter(Context context, Cursor cursor, ColdItemListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    public interface ColdItemListener {
        void onStatusChange(long id, int becoming);
    }

    @NonNull
    @Override
    public ColdTodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cold_item, parent, false);
        return new ColdTodoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ColdTodoViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;
        String description = cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_ITEM));
        long id = cursor.getLong(cursor.getColumnIndex(TodoEntry._ID));

        holder.itemView.setTag(id);
        holder.textViewColdDescription.setText(description);
        holder.imageButtonBecomeUrgentFromCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStatusChange(id, TodoEntry.categoryUrgent);
            }
        });
        holder.imageButtonBecomeNoCold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStatusChange(id, TodoEntry.categoryNormal);
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

    class ColdTodoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewColdDescription;
        ImageButton imageButtonBecomeUrgentFromCold;
        ImageButton imageButtonBecomeNoCold;

        public ColdTodoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewColdDescription = itemView.findViewById(R.id.cold_item_description);
            imageButtonBecomeUrgentFromCold = itemView.findViewById(R.id.become_urgent_fromCold);
            imageButtonBecomeNoCold = itemView.findViewById(R.id.become_no_cold);
        }
    }
}
