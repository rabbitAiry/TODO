package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.data.TodoContract;
import com.example.todo.data.TypeUtils;


public class AllAdapter extends RecyclerView.Adapter<ListAllViewHolder> {
    protected Context context;
    protected Cursor cursor;
    protected final LongPressedListener longPressedListener;
    public final static String ITEM_ID = "ITEM_ID";
    public final static String ITEM_CONTENT = "ITEM_CONTENT";
    public final static String ITEM_TYPE = "ITEM_TYPE";

    public AllAdapter(Context context, Cursor cursor, LongPressedListener listener) {
        this.context = context;
        this.cursor = cursor;
        longPressedListener = listener;
    }

    interface LongPressedListener {
        void gotoEditActivity(Intent intent);
    }

    @NonNull
    @Override
    public ListAllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        return new ListAllViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAllViewHolder holder, int position) {
//        visibility
        holder.itemRelativeLayout.setVisibility(View.INVISIBLE);
        holder.itemToUrgentButton.setVisibility(View.INVISIBLE);
        holder.itemButtonDone.setVisibility(View.INVISIBLE);
//        get data via cursor
        if (!cursor.moveToPosition(position)) return;
        String content = cursor.getString(cursor.getColumnIndex(TodoContract.TodoEntry.TODO_COLUMN_CONTENT));
        int type = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry.TODO_COLUMN_TYPE));
        long id = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoEntry._ID));
//        set data
        holder.itemContentText.setText(content);
        holder.itemStatusBar.setBackgroundColor(TypeUtils.getTypeColor(type, context));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, EditTodoActivity.class);
                intent.putExtra(ITEM_ID, id);
                intent.putExtra(ITEM_CONTENT, content);
                intent.putExtra(ITEM_TYPE, type);
                longPressedListener.gotoEditActivity(intent);
                return false;
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
            notifyDataSetChanged();
        }
    }
}

class ListAllViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout itemRelativeLayout;
    ImageButton itemButtonDone;
    TextView itemContentText;
    ImageView itemToUrgentButton;
    ImageView itemStatusBar;
    ImageView itemDailyImageView;
    TextView itemDailyText;
    View itemView;

    public ListAllViewHolder(@NonNull View itemView) {
        super(itemView);
        itemRelativeLayout = itemView.findViewById(R.id.item_relative_layout);
        itemButtonDone = itemView.findViewById(R.id.item_button_done);
        itemContentText = itemView.findViewById(R.id.item_content_text);
        itemToUrgentButton = itemView.findViewById(R.id.item_to_urgent_button);
        itemStatusBar = itemView.findViewById(R.id.item_status_bar);
        itemDailyImageView = itemView.findViewById(R.id.item_daily_imageview);
        itemDailyText = itemView.findViewById(R.id.item_daily_text);
        this.itemView = itemView;
    }
}

