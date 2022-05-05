package com.example.todo.utils;

import android.content.Context;

import com.example.todo.R;
import com.example.todo.data.TodoItem;

public enum TodoWrapper {
    AllTodoWrapper(TodoTypeUtil.TYPE_ALL, R.string.type_all, R.color.black, null),
    UrgentTodoWrapper(TodoTypeUtil.TYPE_URGENT, R.string.type_urgent, R.color.urgent, new NormalTodoSubmit()),
    PeriodicTodoWrapper(TodoTypeUtil.TYPE_PERIODIC, R.string.type_periodic, R.color.periodic, new PeriodicTodoSubmit()),
    NormalTodoWrapper(TodoTypeUtil.TYPE_NORMAL, R.string.type_normal, R.color.normal, new NormalTodoSubmit()),
    LaterTodoWrapper(TodoTypeUtil.TYPE_LATER, R.string.type_later, R.color.later, new NormalTodoSubmit());

    public int typeId;
    public int typeTextId;
    public int typeColorId;
    public NormalTodoSubmit handler;

    TodoWrapper(int typeId, int typeTextId, int typeColorId, NormalTodoSubmit handler) {
        this.typeId = typeId;
        this.typeTextId = typeTextId;
        this.typeColorId = typeColorId;
    }

    public static TodoWrapper getTypeWrapperById(int typeId) {
        TodoWrapper[] wrappers = values();
        for (TodoWrapper wrapper : wrappers) {
            if (wrapper.typeId == typeId) return wrapper;
        }
        return null;
    }
}