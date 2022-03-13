package com.example.todo.TodoItemWrapper;

import com.example.todo.R;
import com.example.todo.data.TodoItemTypeUtils;

public class TodoTypeLaterWrapper extends TodoItemWrapper {
    public TodoTypeLaterWrapper() {
        typeId = TodoItemTypeUtils.TYPE_LATER;
        typeTextId = R.string.type_later;
        typeColorId = R.color.later;
    }
}
