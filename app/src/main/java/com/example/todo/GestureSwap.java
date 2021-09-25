package com.example.todo;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureSwap extends GestureDetector.SimpleOnGestureListener {
    public static final int SWIPE_LEFTWARD = 1;
    public static final int SWIPE_RIGHTWARD = 2;
    private GestureSwapListener gestureSwapListener;

    interface GestureSwapListener {
        void onFlingListener(int gestureKey);
    }

    public GestureSwap(GestureSwapListener gestureSwapListener) {
        this.gestureSwapListener = gestureSwapListener;
    }

    /**
     * judge the gesture
     *
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) return false;
        int gestureKey = 0;
        if (e1.getX() - e2.getX() > 180) {
            gestureKey = SWIPE_LEFTWARD;
        } else if (e1.getX() - e2.getX() < -180) {
            gestureKey = SWIPE_RIGHTWARD;
        }
        gestureSwapListener.onFlingListener(gestureKey);
        return true;
    }
}
