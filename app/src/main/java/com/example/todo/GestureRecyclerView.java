package com.example.todo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * default RecyclerView has its own gesture and therefore, my gesture of swap can't be capture.
 * In this class, the GestureRecyclerView will judge isn't the gesture is swap first, and then call super.onTouchEvent(e)
 */
public class GestureRecyclerView extends RecyclerView {
    GestureDetector gestureDetector;

    public GestureRecyclerView(@NonNull Context context) {
        super(context);
        gestureDetector = new GestureDetector(new GestureSwap((GestureSwap.GestureSwapListener) context));
    }

    public GestureRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(new GestureSwap((GestureSwap.GestureSwapListener) context));
    }

    public GestureRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(new GestureSwap((GestureSwap.GestureSwapListener) context));
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (gestureDetector.onTouchEvent(e)) return true;
        return super.onTouchEvent(e);
    }
}
