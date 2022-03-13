package com.example.todo.TodoItemWrapper;

import com.example.todo.R;
import com.example.todo.data.TodoItemTypeUtils;

public class TodoTypeNormalWrapper extends TodoItemWrapper {
    public TodoTypeNormalWrapper() {
        typeId = TodoItemTypeUtils.TYPE_NORMAL;
        typeTextId = R.string.type_normal;
        typeColorId = R.color.normal;
    }
}
