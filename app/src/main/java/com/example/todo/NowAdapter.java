package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.todo.data.TodoContract;
import com.example.todo.data.TypeUtils;

public class NowAdapter extends AllAdapter {
    private final NowButtonClickedListener listener;

    public NowAdapter(Context context, Cursor cursor, NowButtonClickedListener listener, LongPressedListener longPressedListener) {
        super(context, cursor, longPressedListener);
        this.listener = listener;
    }

    interface NowButtonClickedListener {
        Cursor onButtonDoneClicked(long id);

        Cursor onButtonToUrgentClicked(long id);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAllViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int type = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.TODO_COLUMN_TYPE));
        int isCreatedByDaily = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.TODO_COLUMN_IS_CREATED_BY_DAILY));
        long id = cursor.getLong(cursor.getColumnIndex(TodoContract.TodoEntry._ID));

        holder.itemRelativeLayout.setVisibility(View.VISIBLE);
        holder.itemButtonDone.setVisibility(View.VISIBLE);
        if (type == TypeUtils.TYPE_URGENT) {
            holder.itemToUrgentButton.setVisibility(View.INVISIBLE);
        } else {
            holder.itemToUrgentButton.setVisibility(View.VISIBLE);
        }
        if (isCreatedByDaily == 1) {
            holder.itemDailyImageView.setVisibility(View.VISIBLE);
            holder.itemDailyText.setVisibility(View.VISIBLE);
        } else {
            holder.itemDailyImageView.setVisibility(View.INVISIBLE);
            holder.itemDailyText.setVisibility(View.INVISIBLE);
        }

        holder.itemButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = listener.onButtonDoneClicked(id);
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.itemToUrgentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = listener.onButtonToUrgentClicked(id);
                swapCursor(cursor);
            }
        });
    }
}