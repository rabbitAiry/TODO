package com.example.todo.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;

public class TodoItemViewHolder extends RecyclerView.ViewHolder {
    ImageButton itemButtonDone;
    TextView itemContentText;
    ImageView itemToUrgentButton;
    View itemTypeBar;
    TextView itemPeriodText;
    View itemView;

    public TodoItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemButtonDone = itemView.findViewById(R.id.item_button_done);
        itemContentText = itemView.findViewById(R.id.item_content_text);
        itemToUrgentButton = itemView.findViewById(R.id.item_to_urgent_button);
        itemTypeBar = itemView.findViewById(R.id.item_type_bar);
        itemPeriodText = itemView.findViewById(R.id.item_period_text);
        this.itemView = itemView;
    }
}
