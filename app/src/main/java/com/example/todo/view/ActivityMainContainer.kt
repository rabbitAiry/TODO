package com.example.todo.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import kotlin.math.abs

class ActivityMainContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var intercepted: Boolean = false
    private var lastX: Int = 0
    private var lastY: Int = 0
    private var firstX: Int = 0
    private lateinit var listener: LayoutSwipeListener

    interface LayoutSwipeListener {
        fun swapCursor(toward: Int)
    }

    fun setSwipeListener(listener: LayoutSwipeListener) {
        this.listener = listener
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                firstX = x
                intercepted = false
            }
            MotionEvent.ACTION_MOVE -> {
                // 如果是左右滑动事件，则直接拦截
                // 在外部拦截法下，fragment内容不应该实现左右滑动
                val offsetX = x - lastX
                val offsetY = y - lastY
                if (offsetX != 0 && offsetY != 0 && abs(offsetX) > abs(offsetY) * 2)
                    intercepted = true
            }
        }
        lastX = x
        lastY = y
        return intercepted
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                val distance = x - firstX
                if (distance > 180) listener.swapCursor(-1)
                else if (distance < -180) listener.swapCursor(1)
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}