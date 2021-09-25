package com.example.todo.data;

import android.content.Context;
import android.content.res.Resources;

import com.example.todo.R;

public class TypeUtils {
    /**
     * type key of todo_item
     */
    public static final int TYPE_ALL = 0;
    public static final int TYPE_URGENT = 1;
    public static final int TYPE_DAILY = 2;
    public static final int TYPE_NORMAL = 3;
    public static final int TYPE_COLD = 5;
    public static final int TYPE_DELETE = 10;

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
            case TYPE_DAILY:
                color = res.getColor(R.color.daily, context.getTheme());
                break;
            case TYPE_COLD:
                color = res.getColor(R.color.cold, context.getTheme());
                break;
        }
        return color;
    }
}
