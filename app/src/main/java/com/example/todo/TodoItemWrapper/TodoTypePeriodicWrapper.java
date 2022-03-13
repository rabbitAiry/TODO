package com.example.todo.TodoItemWrapper;

import com.example.todo.R;
import com.example.todo.data.TodoItemTypeUtils;

public class TodoTypePeriodicWrapper extends TodoItemWrapper {
    public TodoTypePeriodicWrapper() {
        typeId = TodoItemTypeUtils.TYPE_PERIODIC;
        typeTextId = R.string.type_periodic;
        typeColorId = R.color.periodic;
    }
}
