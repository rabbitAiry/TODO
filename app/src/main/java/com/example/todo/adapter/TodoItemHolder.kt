package com.example.todo.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R

class TodoItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var itemButtonDone: ImageButton = itemView.findViewById(R.id.item_button_done)
    var itemContentText: TextView = itemView.findViewById(R.id.item_content_text)
    var itemToUrgentButton: ImageView = itemView.findViewById(R.id.item_to_urgent_button)
    var itemTypeBar: View = itemView.findViewById(R.id.item_type_bar)
}