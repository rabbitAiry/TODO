package com.example.todo.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class TodoItemDataUtil {
    public final static String LATEST_DATE_KEY = "date";
    public final static String SHARED_PREFERENCE = "preference";
    private static TodoDao todoDao;
    private volatile static TodoItemDataUtil instance;

    public static TodoItemDataUtil getInstance(Context context){
        if(instance == null){
            synchronized (TodoItemDataUtil.class){
                if(instance == null){
                    instance = new TodoItemDataUtil(context);
                }
            }
        }
        return instance;
    }

    private TodoItemDataUtil(Context context) {
        TodoDatabase db = TodoDatabase.getInstance(context);
        todoDao = db.todoDao();
        dailyTodoItemCheck(context);
    }

    private void dailyTodoItemCheck(Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        int todayValue = TimeUtils.getTodayToken();
        int latestDateValue = preferences.getInt(LATEST_DATE_KEY, 0);
        if (todayValue > latestDateValue) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(LATEST_DATE_KEY, todayValue).apply();
            // TODO
        }
    }

    public long insertTodo(TodoItem todoItem){
        return todoDao.insertTodo(todoItem);
    }

    public void updateTodoItem(TodoItem todoItem){
        todoDao.updateTodo(todoItem);
    }

    public void updateTodoTypeViaId(long id, int type){
        TodoItem item = todoDao.loadSpecifiedIdTodoItem(id);
        item.type = type;
        todoDao.updateTodo(item);
    }

    public void deleteTodoItem(TodoItem todoItem){
        todoDao.deleteTodo(todoItem);
    }

    public List<TodoItem> getAllTodoItem(){
        return todoDao.loadAllTodoItem();
    }

    public List<TodoItem> getNowTodoItem(){
        return todoDao.loadNowTodoItem(TodoItemTypeUtils.TYPE_URGENT, TodoItemTypeUtils.TYPE_NORMAL);
    }

    public List<TodoItem> getSpecifiedTodoItem(int type){
        if (type == TodoItemTypeUtils.TYPE_ALL){
            return getAllTodoItem();
        }else{
            return todoDao.loadSpecifiedTypeTodoItem(type);
        }
    }
}
