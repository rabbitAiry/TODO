package com.example.todo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActivityMainContainer extends FrameLayout {
    private static final String TAG = "touch_event";
    private boolean intercepted = false;
    private int lastX, lastY;
    private int firstX;
    private LayoutSwipeListener listener;

    public interface LayoutSwipeListener {
        void swapCursor(int toward);
    }

    public void setSwipeListener(LayoutSwipeListener listener) {
        this.listener = listener;
    }

    public ActivityMainContainer(@NonNull Context context) {
        super(context);
    }

    public ActivityMainContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ActivityMainContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ActivityMainContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onInterceptTouchEvent: ACTION_DOWN");
                firstX = x;
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果左右滑动，则将点击事件交由当前view处理；如果上下滑动，则将点击事件交由子级view处理
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                if (offsetX != 0 && offsetY != 0 && Math.abs(offsetX) > Math.abs(offsetY) * 2)
                    intercepted = true;
                Log.d(TAG, "offsetX:" + offsetX + "\toffsetY:" + offsetY);
                Log.d(TAG, "intercepted:" + intercepted);
        }
        lastX = x;
        lastY = y;
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                intercepted = true;
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                int distanceX = x - firstX;
                if (distanceX > 180) listener.swapCursor(-1);
                else if (distanceX < -180) listener.swapCursor(1);
                intercepted = false;
                return true;
        }
        lastX = x;
        return super.onTouchEvent(event);
    }
}