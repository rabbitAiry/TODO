package com.example.todo.data;

import android.content.Context;

import com.example.todo.TodoItemWrapper.TodoItemWrapper;
import com.example.todo.TodoItemWrapper.TodoTypeAllWrapper;
import com.example.todo.TodoItemWrapper.TodoTypeLaterWrapper;
import com.example.todo.TodoItemWrapper.TodoTypeNormalWrapper;
import com.example.todo.TodoItemWrapper.TodoTypePeriodicWrapper;
import com.example.todo.TodoItemWrapper.TodoTypeUrgentWrapper;

public class TodoItemTypeUtils {
    /**
     * type key of todo_item
     */
    public static final int TYPE_ALL = 0;
    public static final int TYPE_URGENT = 1;
    public static final int TYPE_PERIODIC = 2;
    public static final int TYPE_NORMAL = 3;
    public static final int TYPE_LATER = 5;

    public static final int NON_PERIOD = 0;
    public static final int PERIOD_DAY = 1;
    public static final int PERIOD_WEEK = 2;
    public static final int PERIOD_MONTH = 3;
    public static final int PERIOD_YEAR = 4;
    public static final TodoItemWrapper[] todoItemWrappers = new TodoItemWrapper[]{
            new TodoTypeAllWrapper(),
            new TodoTypeUrgentWrapper(),
            new TodoTypePeriodicWrapper(),
            new TodoTypeNormalWrapper(),
            new TodoTypeLaterWrapper()
    };

    public static int getTypeColorByTypeId(int typeId, Context context){
        return getColor(getTypeWrapperById(typeId).typeColorId, context);
    }

    public static int getColor(int typeColorId, Context context){
        return context.getResources().getColor(typeColorId, context.getTheme());
    }

    public static String getText(int typeTextId, Context context){
        return context.getResources().getString(typeTextId);
    }

    public static TodoItemWrapper[] getTypeList() {
        return todoItemWrappers;
    }

    public static TodoItemWrapper getTypeWrapperById(int typeId) {
        TodoItemWrapper wrapper;
        switch (typeId){
            default:
            case TYPE_ALL:
                wrapper = new TodoTypeAllWrapper();
                break;
            case TYPE_NORMAL:
                wrapper = new TodoTypeNormalWrapper();
                break;
            case TYPE_URGENT:
                wrapper = new TodoTypeUrgentWrapper();
                break;
            case TYPE_PERIODIC:
                wrapper = new TodoTypePeriodicWrapper();
                break;
            case TYPE_LATER:
                wrapper = new TodoTypeLaterWrapper();
                break;
        }
        return wrapper;
    }
}
