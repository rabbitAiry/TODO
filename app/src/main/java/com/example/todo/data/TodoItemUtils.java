package com.example.todo.data;

import android.content.Context;
import android.content.res.Resources;

import com.example.todo.R;

public class TodoItemUtils {
    /**
     * type key of todo_item
     */
    public static final int TYPE_ALL = 0;
    public static final int TYPE_URGENT = 1;
    public static final int TYPE_PERIODIC = 2;
    public static final int TYPE_NORMAL = 3;
    public static final int TYPE_LATER = 5;
    public static final int TYPE_DELETE = 10;

    public static final int NON_PERIOD = 0;
    public static final int PERIOD_DAY = 1;
    public static final int PERIOD_WEEK = 2;
    public static final int PERIOD_MONTH = 3;
    public static final int PERIOD_YEAR = 4;

    public static int getTypeColor(int type, Context context) {
        int color = 0;
        Resources res = context.getResources();
        switch (type) {
            case TYPE_NORMAL:
                color = res.getColor(R.color.normal, context.getTheme());
                break;
            case TYPE_URGENT:
                color = res.getColor(R.color.urgent, context.getTheme());
                break;
            case TYPE_PERIODIC:
                color = res.getColor(R.color.daily, context.getTheme());
                break;
            case TYPE_LATER:
                color = res.getColor(R.color.cold, context.getTheme());
                break;
        }
        return color;
    }
}
