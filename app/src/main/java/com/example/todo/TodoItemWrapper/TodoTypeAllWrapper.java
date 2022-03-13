package com.example.todo.TodoItemWrapper;

import com.example.todo.R;
import com.example.todo.data.TodoItemTypeUtils;

public class TodoTypeAllWrapper extends TodoItemWrapper {
    public TodoTypeAllWrapper() {
        typeId = TodoItemTypeUtils.TYPE_ALL;
        typeTextId = R.string.type_all;
        typeColorId = R.color.black;
    }
}
