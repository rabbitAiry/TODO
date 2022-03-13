package com.example.todo.TodoItemWrapper;

import com.example.todo.R;
import com.example.todo.data.TodoItemTypeUtils;

public class TodoTypeUrgentWrapper extends TodoItemWrapper {
    public TodoTypeUrgentWrapper() {
        typeId = TodoItemTypeUtils.TYPE_URGENT;
        typeTextId = R.string.type_urgent;
        typeColorId = R.color.urgent;
    }
}
